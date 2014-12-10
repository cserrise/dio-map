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
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import de.unima.ki.dio.Settings;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Ontology {

	HashSet<Entity> entities = new HashSet<Entity>();
	HashMap<String, Concept> uri2Concept = new HashMap<String, Concept>();  
	private MaxentTagger postagger = new MaxentTagger("nlp/english-caseless-left3words-distsim.tagger");
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
		OWLOntology ontologyOWL = null;
		manager = OWLManager.createOWLOntologyManager();
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		
		try {
			ontologyOWL = manager.loadOntologyFromOntologyDocument(new File(filepath));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontologyOWL);
		
		
		//add classes
		for(OWLClass classy:ontologyOWL.getClassesInSignature()){
			if (classy.getIRI().toString().startsWith(Settings.OWL_NS)) {
				continue;
			}
			ArrayList<Word> words = getLabelAsWordList(classy.getIRI());
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
		
		//add Object Properties
		for(OWLObjectProperty objProperty:ontologyOWL.getObjectPropertiesInSignature()){
			if(objProperty.toString().startsWith(Settings.OWL_NS)){
				continue;
			}
			ArrayList<Word> words = getLabelAsWordList(objProperty.getIRI());
			Label objPropLabel = new Label(words);
			ObjectProperty objProp = new ObjectProperty(objProperty.getIRI().toString(), objPropLabel);
			
			//DOMAIN
			
			HashSet<Concept> domainConcepts = new HashSet<Concept>();
			
			for(OWLClassExpression classEx:objProperty.getDomains(ontologyOWL)){
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
			
			for(OWLClassExpression classEx:objProperty.getRanges(ontologyOWL)){
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
								break;
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
//					System.out.println(domainLabel.toSpacedString() + "D  " + rangeLabel.toSpacedString() + "R");
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
						objProp.setDomainLabel(domainLabel);
					}else if(countRangeCriteria > 0){
						objProp.setRangeLabel(rangeLabel);
					}

				}
			}
//			System.out.println(objProp.toInfoString());
//			System.out.println();
			this.addObjectProperty(objProp);
		}
		
		//add Dataproperties
		for(OWLDataProperty dataPropertyOWL:ontologyOWL.getDataPropertiesInSignature()){
			if(dataPropertyOWL.toString().startsWith(Settings.OWL_NS)){
				continue;
			}
			ArrayList<Word> words = getLabelAsWordList(dataPropertyOWL.getIRI());
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
			sentence = sentence + " " +token;
			words.add(Word.createWord(token));
		}		
		return words;
	}
	

}
