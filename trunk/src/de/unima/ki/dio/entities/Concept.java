package de.unima.ki.dio.entities;

import java.util.HashSet;


public class Concept extends Entity {

	
	private HashSet<Concept> subConcepts = new HashSet<Concept>();
	private HashSet<Concept> disjointConcepts = new HashSet<Concept>();
	
	public Concept(String uri, Label label) {
		super(uri, label);
	}
	
	/**
	* Adds a concept c that is a sub concept of this concept.
	*  
	* @param c The sub concept.
	*/
	public void addSubConcept(Concept c) {
		this.subConcepts.add(c);
	}
	
	/**
	* Adds a concept c that is disjoint with this concept.
	*  
	* @param c The sub concept.
	*/
	public void addDisjointConcept(Concept c) {
		this.disjointConcepts.add(c);
	}
	

	/**
	* Checks whether a given concept c is disjoint with this concept. 
	* 
	* @param c The concept that is or is not disjoint.
	* 
	* @return True if c is disjoint, false otherwise.
	*/
	public boolean isDisjoint(Concept c) {
		if (this.disjointConcepts.contains(c)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	* Checks whether a given concept c is a sub concept of this concept. 
	* 
	* @param c The concept that is or is not a sub concept.
	* 
	* @return True if c is a sub concept, false otherwise.
	*/
	public boolean isSubClass(Concept c) {
		if (this.subConcepts.contains(c)) {
			return true;
		}
		else {
			return false;
		}
		
		
	}
	

	
	


}
