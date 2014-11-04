package de.unima.ki.dio.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import de.unima.ki.dio.Settings;

public class Ontology {

	HashSet<Entity> entities = new HashSet<Entity>();

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
			
			ArrayList<Word> words = getLabelAsWordList(classy);
			Label label = new Label(words);
			Concept concept = new Concept(classy.getIRI().toString(), label);
			
			//add disjointconcepts and subconcepts to concept
			
			for(OWLClass disjointClass:reasoner.getDisjointClasses(classy).getFlattened()){
				if(disjointClass.getIRI().toString().equals(classy.getIRI().toString())){
					continue;
				}
				Label disjointLabel = new Label(getLabelAsWordList(disjointClass));
				Concept disjointConcept = new Concept(disjointClass.getIRI().toString(), disjointLabel);
				concept.addDisjointConcept(disjointConcept);
			}
			

			for(OWLClass subClass:reasoner.getSubClasses(classy, false).getFlattened()){
				if (subClass.getIRI().toString().startsWith(Settings.OWL_NS)) {
					continue;
				}
				Label subClassLabel = new Label(getLabelAsWordList(subClass));
				Concept subConcept = new Concept(subClass.getIRI().toString(), subClassLabel);
				concept.addSubConcept(subConcept);
			}
			
			this.addConcept(concept);
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
	
	
	private ArrayList<Word> getLabelAsWordList(OWLClass classy){
		ArrayList<Word> words = new ArrayList<Word>();
		String[] tokens = classy.getIRI().getFragment().split(Settings.REGEX_FOR_SPLIT);
		for(String token:tokens){
			words.add(Word.createWord(token));
		}
		return words;
	}
	

}
