package de.unima.ki.dio.entities;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import de.unima.ki.dio.Settings;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Ontology {
	
	// used for creating unique ids for labels that appear multiple times in the same ontology
	
	private int idcounter = 1;
	private HashSet<String> labelStore = new HashSet<String>();
	private static final String SUFFIX = "xyxyx";


	HashSet<Entity> entities = new HashSet<Entity>();
	HashMap<String, Concept> uri2Concept = new HashMap<String, Concept>();  
	HashMap<String, ObjectProperty> uri2ObjectProperty = new HashMap<String, ObjectProperty>();  
	HashMap<String, DataProperty> uri2DataProperty = new HashMap<String, DataProperty>();  
	
	
	private MaxentTagger postagger = new MaxentTagger("nlp/english-caseless-left3words-distsim.tagger");
	private OWLOntology ontologyOWL = null;
	/**
	* Constructs an internal representation of the ontology in terms of storing all entities of the ontology
	* with their corresponding labels, which again consist of a list of words.
	*  
	* @param filepath
	*/
	public Ontology(String filepath) {
		// first reset the store to avoid taking over concepts from a previously generated ontology
		Word.resetStore();
		// construct words, labels, concepts as shown in the usage example and add the concepts to the ontology
		// TODO implement this
		
		OWLOntologyManager manager;
		manager = OWLManager.createOWLOntologyManager();
		PelletReasonerFactory reasonerFactory =  new PelletReasonerFactory();
		try {
			ontologyOWL = manager.loadOntologyFromOntologyDocument(new File(filepath));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
		//this.reasoner = reasonerFactory.createReasoner(ontology);
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontologyOWL);
		
		
		//add classes
		for(OWLClass classy:ontologyOWL.getClassesInSignature()){
			if (classy.getIRI().toString().startsWith(Settings.OWL_NS)) {
				continue;
			}
			ArrayList<Word> words = getLabelAsWordList(classy.getIRI());
			if (labelStore.contains(words.toString())) { makeNonUniqueWordsUnique(words); }
			else { labelStore.add(words.toString()); }
			Label label = new Label(words);
			Concept concept = new Concept(classy.getIRI().toString(), label);
			this.addConcept(concept);
			this.uri2Concept.put(classy.getIRI().toString(), concept);
		}
		
		for(OWLClass classy:ontologyOWL.getClassesInSignature()){
			if (classy.getIRI().toString().startsWith(Settings.OWL_NS)) {
				continue;
			}
			Concept concept = getConceptByUri(classy.getIRI().toString());
			addSemantics(reasoner, classy, concept);
		}
		
		//add object properties
		for(OWLObjectProperty objPropertyOWL:ontologyOWL.getObjectPropertiesInSignature()){
			if(objPropertyOWL.toString().startsWith(Settings.OWL_NS)){
				continue;
			}
			
			ArrayList<Word> words = getLabelAsWordList(objPropertyOWL.getIRI());
			if (labelStore.contains(words.toString())) { makeNonUniqueWordsUnique(words); }
			else { labelStore.add(words.toString()); }
			
			
			
			Label objPropLabel = new Label(words);
			ObjectProperty objProp = new ObjectProperty(objPropertyOWL.getIRI().toString(), objPropLabel);
			
			//DOMAIN
			
			HashSet<Concept> domainConcepts = new HashSet<Concept>();
			
			for(OWLClassExpression classEx:objPropertyOWL.getDomains(ontologyOWL)){
				if(classEx.isAnonymous()){
					objProp.setDomainAnonymous(true);
					for(OWLClass domClass:classEx.getClassesInSignature()){
						domainConcepts.add(getConceptByUri(domClass.getIRI().toString()));
					}
				}else{
					OWLClass domain = classEx.asOWLClass();
					domainConcepts.add(getConceptByUri(domain.getIRI().toString()));
				}
			}
			objProp.setDomainConcept(domainConcepts);
			
			//RANGE
			
			HashSet<Concept> rangeConcepts = new HashSet<Concept>();
			
			for(OWLClassExpression classEx:objPropertyOWL.getRanges(ontologyOWL)){
				if(classEx.isAnonymous()){
					objProp.setRangeAnonymous(true);
					for(OWLClass domClass:classEx.getClassesInSignature()){
						rangeConcepts.add(getConceptByUri(domClass.getIRI().toString()));
					}
				}else{
					OWLClass range = classEx.asOWLClass();
					rangeConcepts.add(getConceptByUri(range.getIRI().toString()));
				}
			}
			objProp.setRangeConcept(rangeConcepts);
			
			//Extra NLP stuff object property
			
			for(Concept domain:domainConcepts){
				for(Concept range:rangeConcepts){
					Label domainLabel = domain.getLabels().iterator().next();
					Label rangeLabel = range.getLabels().iterator().next();
					
					String sentence=domainLabel.toSpacedString() + " " + objPropLabel.toSpacedString() + " " + rangeLabel.toSpacedString();
					for(List<HasWord> list:MaxentTagger.tokenizeText(new StringReader(sentence))){
						List<TaggedWord> taggedWordList = postagger.tagSentence(list);
						Morphology m = new Morphology();
						for(int i = 0; i < taggedWordList.size(); i++){
							if(taggedWordList.get(i).tag().contains("VB")){
								objProp.setVerbBaseForm(m.lemma(taggedWordList.get(i).word(), taggedWordList.get(i).tag()));
								break;//depending of either emphasize earlier or later verbs 
							}
						}
					}
					int countDomain = 0;
					int countRange = 0;
					for(int i = 0; i < domainLabel.getNumberOfWords(); i++){
						if(objPropLabel.toSpacedString().contains(domainLabel.getWord(i).getToken())){
							countDomain++;
						}
					}
					for(int i = 0; i < rangeLabel.getNumberOfWords(); i++){
						if(objPropLabel.toSpacedString().contains(rangeLabel.getWord(i).getToken())){
							countRange++;
						}
					}
//					System.out.println(domainLabel.toSpacedString() + "D  "+ objPropLabel.toSpacedString() + rangeLabel.toSpacedString() + "R");
					int countDomainCriteria = 0;
					int countRangeCriteria = 0;
					//criteria 1 -> matching tokens with atleast more than 0 (favors range when equal)
					if(countDomain > countRange && countDomain > 0){
						countDomainCriteria++;
					}else if(countRange > 0){
						countRangeCriteria++;
					}
					//criteria 2 -> how many tokens are percentagewise in the objProp label (needs to be clearly)
					if(countDomain/domainLabel.getNumberOfWords() > countRange/rangeLabel.getNumberOfWords()){
						countDomainCriteria++;
					}else if(countRange/rangeLabel.getNumberOfWords() > countDomain/domainLabel.getNumberOfWords()){
						countRangeCriteria++;
					}
					//Decision -> favors rangecritera when equal. 
					if(countDomainCriteria > countRangeCriteria && countDomainCriteria > 0){
						objProp.setDomainLabelX(domainLabel);
					}else if(countRangeCriteria > 0){
						objProp.setRangeLabelX(rangeLabel);
					}
					
					
					//Ab hier neu
					//Es wird die Object Property annotiert vom POS tagger und dann einmal geschaut wo "B of A -> AB" ist und einmal wo das verb steht und dann der index davon gespeichert.
					Label logicalDomainLabel = null;
					Label logicalRangeLabel = null;
					ArrayList<Word> wordsForDomainLabel = new ArrayList<Word>();
					ArrayList<Word> wordsForRangeLabel = new ArrayList<Word>();
					int index = -1;
					List<TaggedWord> taggedWordList = null;
					for(List<HasWord> list:MaxentTagger.tokenizeText(new StringReader(objPropLabel.toSpacedString()))){
						taggedWordList = postagger.tagSentence(list);
						Morphology m = new Morphology();
						for(int i = 1; i < taggedWordList.size() - 1; i++){
							if(taggedWordList.get(i).word().contains("of")){
								objProp.setRangeLabel(new Label(Word.createWord(taggedWordList.get(i+1).word(), getWordtypeForTag(taggedWordList.get(i+1).tag())), Word.createWord(taggedWordList.get(i-1).word(), getWordtypeForTag(taggedWordList.get(i-1).tag()))));
							}
						}
						for(int i = 0; i < taggedWordList.size(); i++){
							if(taggedWordList.get(i).tag().contains("VB")){
								index = i;
								break;
							}
						}
					}
					//Wenn kein verb gefunden wurde ist der index immer noch -1 und es ist quasi nicht m�glich zusagen was zur logical domain/range geh�rt. 
					//Ausser du willst die Annahme treffen das dann tendenziell alles zur range geh�rt dann kannst du das hier �ndern. Sonst wird nach w�rter die formen von NN(noun)
					//sind bzw. JJ(Adjektiven) gesucht. Und je nachdem ob sie vor oder nach dem gefundenen verb stehen der domain oder der range hinzugef�gt.
					if(index == -1){
						break;
					}else{
						for(int i = index+1; i < taggedWordList.size(); i++){
							String tag = taggedWordList.get(i).tag();
							if(tag.contains("NN") || tag.contains("JJ")){
								wordsForRangeLabel.add(Word.createWord(taggedWordList.get(i).word(), getWordtypeForTag(taggedWordList.get(i).tag())));
							}
						}
						if (index > 0){
							for(int i = 0; i < index; i++){
								String tag = taggedWordList.get(i).tag();
								if(tag.contains("NN") || tag.contains("JJ")){
									wordsForDomainLabel.add(Word.createWord(taggedWordList.get(i).word(),getWordtypeForTag(taggedWordList.get(i).tag())));
								}
							}
						}
					}
					//Wenn nix gefunden wurde wird nichts gemacht. Also ist das Attribut in der Object Property klasse null.
					if(wordsForDomainLabel.size() > 0){
						logicalDomainLabel = new Label(wordsForDomainLabel);
						objProp.setDomainLabel(logicalDomainLabel);
						
						for(OWLClass classy:ontologyOWL.getClassesInSignature()){
							String fragment = classy.getIRI().getFragment();
							if(wordsForDomainLabel.size() != fragment.split(Settings.REGEX_FOR_SPLIT).length) continue;
							fragment = fragment.toLowerCase();
							boolean check = true;
							for(Word word:wordsForDomainLabel){
								if(!fragment.contains(word.getToken())){
									check = false;
								}
							}
							if(check){
								objProp.setConceptOfDomainLabel(getConceptByUri(classy.getIRI().toURI().toString()));
							}
						}
						
					}
					if(wordsForRangeLabel.size() > 0){
						logicalRangeLabel = new Label(wordsForRangeLabel);
						objProp.setRangeLabel(logicalRangeLabel);
						
						for(OWLClass classy:ontologyOWL.getClassesInSignature()){
							String fragment = classy.getIRI().getFragment();
							if(wordsForRangeLabel.size() != fragment.split(Settings.REGEX_FOR_SPLIT).length) continue;
							fragment = fragment.toLowerCase();
							boolean check = true;
							for(Word word:wordsForRangeLabel){
								if(!fragment.contains(word.getToken())){
									check = false;
								}
							}
							if(check){
								objProp.setConceptOfRangeLabel(getConceptByUri(classy.getIRI().toURI().toString()));
							}
						}
					}		
				}
			}			
			this.addObjectProperty(objProp);
		}
		
		//add Dataproperties
		for(OWLDataProperty dataPropertyOWL:ontologyOWL.getDataPropertiesInSignature()){
			if(dataPropertyOWL.toString().startsWith(Settings.OWL_NS)){
				continue;
			}
			ArrayList<Word> words = getLabelAsWordList(dataPropertyOWL.getIRI());
			if (labelStore.contains(words.toString())) { makeNonUniqueWordsUnique(words); }
			else { labelStore.add(words.toString()); }
			Label dataPropLabel = new Label(words);
			DataProperty dataProp = new DataProperty(dataPropertyOWL.getIRI().toString(), dataPropLabel);
			
			//domain
			
			HashSet<Concept> domainConcepts = new HashSet<Concept>();
			
			for(OWLClassExpression classEx:dataPropertyOWL.getDomains(ontologyOWL)){
				if(classEx.isAnonymous()){
					dataProp.setDomainAnonymous(true);
					for(OWLClass domClass:classEx.getClassesInSignature()){
						domainConcepts.add(getConceptByUri(domClass.getIRI().toString()));
					}
				}else{
					OWLClass domain = classEx.asOWLClass();
					domainConcepts.add(getConceptByUri(domain.getIRI().toString()));
				}
			}
			dataProp.setDomainConcept(domainConcepts);
			
			//set verbbase form and get logicalrange
			//Hier wird einmal die baseform f�r das verb gesetzt und geschaut wo das verb steht
			int index = -1;
			List<TaggedWord> taggedWordList = null;
			for(List<HasWord> list:MaxentTagger.tokenizeText(new StringReader(dataPropLabel.toSpacedString()))){
				taggedWordList = postagger.tagSentence(list);
				Morphology m = new Morphology();
				for(int i = 0; i < taggedWordList.size(); i++){
					if(taggedWordList.get(i).tag().contains("VB")){
						dataProp.setVerbBaseForm(m.lemma(taggedWordList.get(i).word(), taggedWordList.get(i).tag()));
						index = i;
					}
				}
			}
			//Wenn kein Verb gefunden wurde also index noch -1 ist, nehm ich an das es keins gibt (ist oft so das die property nur "email" oder "name") hei�t und f�ge das der logical range zu.
			//Wenn ein verb gefunden wurde werden in der ganzen property nach nomen und adjektiven gesucht und diese hinzugef�gt
			ArrayList<Word> wordsForRange = new ArrayList<Word>();
			if(index > -1){
				for(int i = index+1; i < taggedWordList.size(); i++){
					wordsForRange.add(Word.createWord(taggedWordList.get(i).word(), getWordtypeForTag(taggedWordList.get(i).tag())));	
				}
			}else{
				for(int i = 0; i < taggedWordList.size(); i++){
					if(taggedWordList.get(i).tag().contains("NN") || taggedWordList.get(i).tag().contains("JJ")){
						wordsForRange.add(Word.createWord(taggedWordList.get(i).word(), getWordtypeForTag(taggedWordList.get(i).tag())));
					}
				}
			}
			//Wenn bei beiden nichts gefunden wurde wird nichts gesetzt
			if(wordsForRange.size() > 0){
				dataProp.setLogicalRangeLabel(new Label(wordsForRange));
			}
			
			//define the datatype of the dataprop
			//Hier werden die datentypen gesetzt. 
			for(OWLDataRange datatype:dataPropertyOWL.getRanges(ontologyOWL)){
				if(!datatype.isDatatype()) break;
				
				String fragment = datatype.asOWLDatatype().getIRI().getFragment();
				if(fragment.contains("date")){
					dataProp.setDatatype(Datatype.DATE);
				}else if(fragment.contains("string")){
					dataProp.setDatatype(Datatype.STRING);
				}else if(fragment.contains("int")){
					dataProp.setDatatype(Datatype.INT);
				}else if(fragment.contains("boolean")){
					dataProp.setDatatype(Datatype.BOOLEAN);
				}else if(fragment.contains("double")){
					dataProp.setDatatype(Datatype.DOUBLE);
				}else if(fragment.contains("float")){
					dataProp.setDatatype(Datatype.FLOAT);
				}else if(fragment.contains("long")){
					dataProp.setDatatype(Datatype.LONG);
				}
			}
			
			this.addDataProperty(dataProp);
		}
			
	}
	


	private Concept getConceptByUri(String uri) {
		if (this.uri2Concept.containsKey(uri)) {
			return this.uri2Concept.get(uri);
		}
		else {
			return null;
		}
	}

	private void addSemantics(OWLReasoner reasoner, OWLClass classy, Concept concept) {
		//add disjointconcepts and subconcepts to concept
		for(OWLClass disjointClass : reasoner.getDisjointClasses(classy).getFlattened()){
			if(disjointClass.getIRI().toString().equals(classy.getIRI().toString())){
				continue;
			}
			Concept disConcept = this.getConceptByUri(disjointClass.getIRI().toString());
			if (disConcept != null) {
				concept.addDisjointConcept(disConcept);
			}
		}
	
		for(OWLClass subClass : reasoner.getSubClasses(classy, false).getFlattened()){
			if (subClass.getIRI().toString().startsWith(Settings.OWL_NS)) {
				continue;
			}
			Concept subConcept = this.getConceptByUri(subClass.getIRI().toString());
			if (subConcept != null) {
				concept.addSubConcept(subConcept);
			}
		}
	}
	
	/**
	 * This constructor is used only for test purpose as long as the standard constructor is not implemented. 
	 * 
	 */
	public Ontology() {
		// first reset the store to avoid taking over concepts from a previously generated ontology
		Word.resetStore();
		
		
	}
	
	/**
	* Iterates over all entities and returns the assiciated words of theoir labels.
	* 
	* @return All words that occur in the labels of the entities of this ontology.
	*/
	public HashSet<Word> getWords() {
		HashSet<Word> words = new HashSet<Word>();
		for (Entity entity : this.entities) {
			HashSet<Label> labels = entity.getLabels();
			for (Label label : labels) {
				for (int i = 0; i < label.getNumberOfWords(); i++) {
					words.add(label.getWord(i));
				}	
			}	
		}
		return words;
	}


	public void addConcept(Concept concept) {
		this.entities.add(concept);
	}
	
	public void addObjectProperty(ObjectProperty objProperty){
		this.entities.add(objProperty);
	}
	
	public void addDataProperty(DataProperty dataProperty){
		this.entities.add(dataProperty);
	}
	
	public HashSet<Entity> getEntities() {
		return this.entities;
	}
	
	public String toString() {
		String rep = "";
		for (Entity entity : this.entities) {
			rep += entity + "\n";
		}
		return rep;
	}
	
	
	private ArrayList<Word> getLabelAsWordList(IRI iri){
		ArrayList<Word> words = new ArrayList<Word>();
		String[] tokens = iri.getFragment().split(Settings.REGEX_FOR_SPLIT);
		String sentence = "";
		for(String token:tokens){
			sentence = sentence + " " + token;
		}	
		for(List<HasWord> list:MaxentTagger.tokenizeText(new StringReader(sentence))){
			List<TaggedWord> taggedWordList = postagger.tagSentence(list);
			Morphology m = new Morphology();
			for(int i = 0; i < taggedWordList.size(); i++){
				words.add(Word.createWord(taggedWordList.get(i).word(), this.getWordtypeForTag(taggedWordList.get(i).tag())));		
			}
		}
		return words;
	}
	
	private WordType getWordtypeForTag(String tag){
		if(tag.contains("VB")){
			return WordType.VERB;
		}else if(tag.contains("NN")){
			return WordType.NOUN;
		}else if(tag.contains("JJ")){
			return WordType.ADJECTIVE;
		}else{
			return WordType.UNKNOWN;
		}
	}
	
	private void makeNonUniqueWordsUnique(ArrayList<Word> words) {
		System.out.println("Set unique: " + words);
		this.idcounter++;
		for (int i = 0; i < words.size(); i++) {
			Word word = words.get(i);
			Word w = Word.createNewWord(word.getToken(), idcounter);
			w.setWordType(word.getType());
			words.set(i, w);
		}		
	}
	

}