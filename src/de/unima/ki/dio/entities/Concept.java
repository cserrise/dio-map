package de.unima.ki.dio.entities;

import java.util.HashSet;
import java.util.Set;


public class Concept extends Entity {

	
	private HashSet<Concept> subConcepts = new HashSet<Concept>();
	private HashSet<Concept> dSubConcepts = new HashSet<Concept>();
	private HashSet<Concept> disjointConcepts = new HashSet<Concept>();
	
	private boolean rootnode = false;
	
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
	* Adds a concept c that is a direct sub concept of this concept.
	*  
	* @param c The sub concept.
	*/
	public void addDSubConcept(Concept c) {
		this.dSubConcepts.add(c);
	}
	
	/**
	* Adds a concept c that is disjoint with this concept.
	*  
	* @param c The sub concept.
	*/
	public void addDisjointConcept(Concept c) {
		this.disjointConcepts.add(c);
	}
	
	public Set<Concept> getSubConcepts() {
		return this.subConcepts;
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
	public boolean isSubConcept(Concept c) {
		if (this.subConcepts.contains(c)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	* Checks whether a given concept c is a direct sub concept of this concept. 
	* 
	* @param c The concept that is or is not a direct sub concept.
	* 
	* @return True if c is a direct sub concept, false otherwise.
	*/
	public boolean isDSubConcept(Concept c) {
		if (this.dSubConcepts.contains(c)) {
			return true;
		}
		else {
			return false;
		}
		
		
	}
	
	/**
	* Checks if the concept is a leafnode by checking whether there are subconcepts 
	* 
	* @return True if this concept is a leafnode, false otherwise.
	*/
	public boolean isLeafnode() {
		return this.subConcepts.isEmpty();
	}
	
	/**
	* 
	* @return True if this concept is a root node, i.e., if it has not super concepts.
	*/
	public boolean isRootnode() {
		return this.rootnode;
	}
	
	/**
	* 
	* @return True if this concept is a root node, i.e., if it has not super concepts.
	*/
	public void setRootnode(boolean rootnode) {
		this.rootnode = rootnode;
	}



}
