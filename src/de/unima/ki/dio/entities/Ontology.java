package de.unima.ki.dio.entities;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

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
	
	HashMap<String, HashSet<String>> differentFrom = null;
	
	private Concept MY_TOP;
	
	private MaxentTagger postagger = new MaxentTagger("nlp/english-caseless-left3words-distsim.tagger");
	private OWLOntology ontologyOWL = null;
	
	
	public void multiplyEntities() {
		
		
		
	}
	
	
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
		
		
		Concept MY_TOP = new Concept(filepath + "#TOP", new Label(new ArrayList<Word>()));
		MY_TOP.setRootnode(true);
		this.addConcept(MY_TOP);
		this.uri2Concept.put(filepath + "#TOP", MY_TOP);
		
		
		//add classes
		for(OWLClass classy:ontologyOWL.getClassesInSignature()){
			if (classy.getIRI().toString().startsWith(Settings.OWL_NS)) {
				continue;
			}
			ArrayList<Word> words = getLabelAsWordList("C", classy.getIRI());
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
			
			ArrayList<Word> words = getLabelAsWordList("O", objPropertyOWL.getIRI());
			if (labelStore.contains(words.toString())) { makeNonUniqueWordsUnique(words); }
			else { labelStore.add(words.toString()); }
			
			
			
			Label objPropLabel = new Label(words);
			ObjectProperty objProp = new ObjectProperty(objPropertyOWL.getIRI().toString(), objPropLabel);
			
			this.uri2ObjectProperty.put(objPropertyOWL.getIRI().toString(), objProp);
			
			//DOMAIN
			
			HashSet<Concept> domainConcepts = new HashSet<Concept>();
			
			for(OWLClassExpression classEx : objPropertyOWL.getDomains(ontologyOWL)){
				if(classEx.isAnonymous()){
					objProp.setDomainAnonymous(true);
					for(OWLClass domClass:classEx.getClassesInSignature()) {
						domainConcepts.add(getConceptByUri(domClass.getIRI().toString()));
					}
				}
				else{
					OWLClass domain = classEx.asOWLClass();
					domainConcepts.add(getConceptByUri(domain.getIRI().toString()));
				}
			}
			if (domainConcepts.size() == 0) { domainConcepts.add(MY_TOP); }
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
			if (rangeConcepts.size() == 0) { rangeConcepts.add(MY_TOP); }
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
					int d = domainLabel.getNumberOfWords() > 0 ? domainLabel.getNumberOfWords() : 1;
					int r = rangeLabel.getNumberOfWords() > 0 ? rangeLabel.getNumberOfWords() : 1;
					if(countDomain/d > countRange/r) {
						countDomainCriteria++;
					}else if(countRange/r > countDomain/d){
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
								objProp.setRangeLabel(new Label(Word.createWord("C", taggedWordList.get(i+1).word(), getWordtypeForTag(taggedWordList.get(i+1).tag())), Word.createWord("C", taggedWordList.get(i-1).word(), getWordtypeForTag(taggedWordList.get(i-1).tag()))));
							}
						}
						for(int i = 0; i < taggedWordList.size(); i++){
							if(taggedWordList.get(i).tag().contains("VB")){
								index = i;
								break;
							}
						}
					}
					//Wenn kein verb gefunden wurde ist der index immer noch -1 und es ist quasi nicht möglich zusagen was zur logical domain/range gehört. 
					//Ausser du willst die Annahme treffen das dann tendenziell alles zur range gehört dann kannst du das hier ändern. Sonst wird nach wörter die formen von NN(noun)
					//sind bzw. JJ(Adjektiven) gesucht. Und je nachdem ob sie vor oder nach dem gefundenen verb stehen der domain oder der range hinzugefügt.
					if(index == -1){
						break;
					}else{
						for(int i = index+1; i < taggedWordList.size(); i++){
							String tag = taggedWordList.get(i).tag();
							if(tag.contains("NN") || tag.contains("JJ")){
								wordsForRangeLabel.add(Word.createWord("C", taggedWordList.get(i).word(), getWordtypeForTag(taggedWordList.get(i).tag())));
							}
						}
						if (index > 0){
							for(int i = 0; i < index; i++){
								String tag = taggedWordList.get(i).tag();
								if(tag.contains("NN") || tag.contains("JJ")){
									wordsForDomainLabel.add(Word.createWord("C", taggedWordList.get(i).word(),getWordtypeForTag(taggedWordList.get(i).tag())));
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
			ArrayList<Word> words = getLabelAsWordList("D", dataPropertyOWL.getIRI());
			if (labelStore.contains(words.toString())) { makeNonUniqueWordsUnique(words); }
			else { labelStore.add(words.toString()); }
			Label dataPropLabel = new Label(words);
			DataProperty dataProp = new DataProperty(dataPropertyOWL.getIRI().toString(), dataPropLabel);
			
			
			this.uri2DataProperty.put(dataPropertyOWL.getIRI().toString(), dataProp);
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
			//Hier wird einmal die baseform für das verb gesetzt und geschaut wo das verb steht
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
			//Wenn kein Verb gefunden wurde also index noch -1 ist, nehm ich an das es keins gibt (ist oft so das die property nur "email" oder "name") heißt und füge das der logical range zu.
			//Wenn ein verb gefunden wurde werden in der ganzen property nach nomen und adjektiven gesucht und diese hinzugefügt
			ArrayList<Word> wordsForRange = new ArrayList<Word>();
			if(index > -1){
				for(int i = index+1; i < taggedWordList.size(); i++){
					wordsForRange.add(Word.createWord("C", taggedWordList.get(i).word(), getWordtypeForTag(taggedWordList.get(i).tag())));	
				}
			}else{
				for(int i = 0; i < taggedWordList.size(); i++){
					if(taggedWordList.get(i).tag().contains("NN") || taggedWordList.get(i).tag().contains("JJ")){
						wordsForRange.add(Word.createWord("C", taggedWordList.get(i).word(), getWordtypeForTag(taggedWordList.get(i).tag())));
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
		addInverseProperties();
		
			
	}
	
	private void addInverseProperties() {
		for(OWLObjectProperty objPropertyOWL : ontologyOWL.getObjectPropertiesInSignature()){
			if(objPropertyOWL.toString().startsWith(Settings.OWL_NS)) {continue; }
			Set<OWLObjectPropertyExpression> inverses = objPropertyOWL.getInverses(this.ontologyOWL);
			for (OWLObjectPropertyExpression inverse : inverses) {
				if (inverse instanceof OWLObjectProperty) {
					ObjectProperty e1 = this.uri2ObjectProperty.get(objPropertyOWL.getIRI().toURI().toString());
					ObjectProperty e2 = this.uri2ObjectProperty.get(((OWLObjectProperty)inverse).getIRI().toURI().toString());
					e1.setInverse(e2);		
				}	
			}
			
			/*
			ArrayList<Word> words = getLabelAsWordList("O", objPropertyOWL.getIRI());
			if (labelStore.contains(words.toString())) { makeNonUniqueWordsUnique(words); }
			else { labelStore.add(words.toString()); }
			
			
			
			Label objPropLabel = new Label(words);
			ObjectProperty objProp = new ObjectProperty(objPropertyOWL.getIRI().toString(), objPropLabel);
			*/
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
			if (disConcept != null && disConcept.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
				concept.addDisjointConcept(disConcept);
			}
		}
		// all subclasses
		for(OWLClass subClass : reasoner.getSubClasses(classy, false).getFlattened()){
			if (subClass.getIRI().toString().startsWith(Settings.OWL_NS)) {
				continue;
			}
			Concept subConcept = this.getConceptByUri(subClass.getIRI().toString());
			if (subConcept != null && subConcept.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
				concept.addSubConcept(subConcept);
			}
		}
		// only the direct ones
		for(OWLClass subClass : reasoner.getSubClasses(classy, true).getFlattened()){
			if (subClass.getIRI().toString().startsWith(Settings.OWL_NS)) {
				continue;
			}
			Concept subConcept = this.getConceptByUri(subClass.getIRI().toString());
			if (subConcept != null && subConcept.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
				concept.addDSubConcept(subConcept);
			}
		}
		
		// set rootnodes
		boolean rootnode = true;
		for(OWLClass superClass : reasoner.getSuperClasses(classy, false).getFlattened()){
			if (superClass.getIRI().toString().startsWith(Settings.OWL_NS)) {
				continue;
			}
			rootnode = false;
			break;
		}
		concept.setRootnode(rootnode);
		
		
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
	
	private void initiDifferentFrom() { 
		this.differentFrom = new HashMap<String, HashSet<String>>();
		for (Concept c1 : this.uri2Concept.values()) {
			HashSet<String> diff = new HashSet<String>();
			String t1 = getSingleToken(c1);
			if (t1 == null) continue;
			for (Concept c2 : this.uri2Concept.values()) {
				String t2 = getSingleToken(c2);
				String h2 = getHeadnounOf2Token(c2);
				String m2 = getModifierOf2Token(c2);
				if (c1.isSubConcept(c2) || c2.isSubConcept(c1)) continue;
				if (t2 != null && !t1.equals(t2)) {
					diff.add(t2);
				}
				if (h2 != null && !t1.equals(h2) && m2.equals(t1)) {
					diff.add(t2);
				}
				
			}
			if (t1 != null) {
				this.differentFrom.put(t1,diff);
			}
		}
	
	}
	
	private String getSingleToken(Concept c) {
		for (Label l : c.getLabels()) {
			if (l.getNumberOfWords() == 1) {
				return l.getWord(0).getToken();
			}
		}
		return null;
	}
	
	private String getHeadnounOf2Token(Concept c) {
		for (Label l : c.getLabels()) {
			if (l.getNumberOfWords() == 2) {
				return l.getWord(1).getToken();
			}
		}
		return null;
	}
	
	private String getModifierOf2Token(Concept c) {
		for (Label l : c.getLabels()) {
			if (l.getNumberOfWords() == 2) {
				return l.getWord(0).getToken();
			}
		}
		return null;
	}
	
	
	
	/**
	* Returns true, if two strings have (with a very high probabvility) a different meaning. This method is implemented
	* on the assumption that the words used to describe two one-word sibling concepts (sibling in this ontology) have a
	* different meaning
	* 
	* @param s1 One word.
	* @param s2 Another word
	* 
	* @return True, if the two words have a different meaning.
	*/
	public boolean haveDifferentMeaning(String s1, String s2) {
		
		if (this.differentFrom == null) initiDifferentFrom();
		if (this.differentFrom.containsKey(s1)) {
			if (this.differentFrom.get(s1).contains(s2)) {
				return true;
			}
		}
		if (this.differentFrom.containsKey(s2)) {
			if (this.differentFrom.get(s2).contains(s1)) {
				return true;
			}
		}
		return false;
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
	
	
	private ArrayList<Word> getLabelAsWordList(String prefix, IRI iri){
		ArrayList<Word> words = new ArrayList<Word>();
		String[] tokens = iri.getFragment().split(Settings.REGEX_FOR_SPLIT);
		tokens = getFlippedOfTokenList(tokens);
		String sentence = "";
		for(String token:tokens){
			sentence = sentence + " " + token;
		}	
		for(List<HasWord> list:MaxentTagger.tokenizeText(new StringReader(sentence))){
			List<TaggedWord> taggedWordList = postagger.tagSentence(list);
			Morphology m = new Morphology();
			for(int i = 0; i < taggedWordList.size(); i++){
				words.add(Word.createWord(prefix, taggedWordList.get(i).word(), this.getWordtypeForTag(taggedWordList.get(i).tag())));		
			}
		}
		return words;
	}
	
	/**
	 * Returns the input array, if it does not contain an "of". Otherwise it creates and returns a copy
	 * where "of" is removed and the part before is flipped with the part after the "of".
	 * 
	 * @param token The token array to be modified
	 * @return An copy of the input array with "of" removed and flipped sections.
	 */
	private String[] getFlippedOfTokenList(String[] token) {
		boolean foundOf = false;
		int ofIndex;
		for (ofIndex = 0; ofIndex < token.length; ofIndex++) {
			if (token[ofIndex].toLowerCase().equals("of")) {
				foundOf = true;
				break;
			}
		}
		// no "of" found return input (or "of" is at the beginning or end) 
		if (!foundOf || (ofIndex == 0 || ofIndex == token.length-1)) {	return token; }
		// "of has been found, do flip
		int flippedIndex = 0;
		String[] flipped = new String[token.length-1];
		for (int i = ofIndex+1; i < token.length; i++) {
			flipped[flippedIndex] = token[i]; 
			flippedIndex++;		
		}
		for (int i = 0; i < ofIndex; i++) {
			flipped[flippedIndex] = token[i]; 
			flippedIndex++;					
		}
		return flipped;
	}
	
	private WordType getWordtypeForTag(String tag){
		if(tag.contains("VB")){
			return WordType.VERB;
		} else if (tag.equals("NN")  || tag.equals("NNP")) {
			return WordType.NOUN_SINGULAR;
		} else if (tag.equals("NNS")  || tag.equals("NNPS")) {
			return WordType.NOUN_PLURAL;
		} else if(tag.contains("JJ")){
			return WordType.ADJECTIVE;
		} else {
			return WordType.UNKNOWN;
		}
	}
	
	private void makeNonUniqueWordsUnique(ArrayList<Word> words) {
		System.out.println("Set unique: " + words);
		this.idcounter++;
		for (int i = 0; i < words.size(); i++) {
			Word word = words.get(i);
			Word w = Word.createNewWord(word.getPrefix(), word.getToken(), idcounter);
			w.setWordType(word.getType());
			words.set(i, w);
		}		
	}
	/*
	public static void main(String[] args) {
		String[] token1 = {"Men", "OF", "war"};
		String[] flipped = getFlippedOfTokenList(token1);
		for (String f : flipped) {
			System.out.println(f);	
		}
		
		
		
	}
	*/
	

}
