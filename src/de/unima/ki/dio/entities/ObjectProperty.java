package de.unima.ki.dio.entities;

import java.util.HashSet;

public class ObjectProperty extends Entity {
	
	private HashSet<Concept> domainConcept = new HashSet<Concept>();
	private HashSet<Concept> rangeConcept = new HashSet<Concept>();
	
	private ObjectProperty inverse = null;
	
	/**
	* If false, there is only one element in the domainConcept set.
	*/
	private boolean domainAnonymous = false;
	
	/**
	* If false, there is only one element in the rangeConcept set.
	*/
	private boolean rangeAnonymous = false;
	
	private String verbBaseForm = null;
	private Label domainLabelX = null;
	private Label rangeLabelX = null;
	
	private Label domainLabel = null;
	private Label rangeLabel = null;
	
	private Concept conceptOfDomainLabel = null;
	private Concept conceptOfRangeLabel = null;
	
	public ObjectProperty(String uri, Label label) {
		super(uri, label);
	}

	/**
	 * returns if the domain of the object property is anonymous 
	 */
	public boolean isDomainAnonymous() {
		return domainAnonymous;
	}
	
	public void setInverse(ObjectProperty p) {
		this.inverse = p;
	}
	
	public ObjectProperty getInverse() {
		return this.inverse;
	}

	/**
	 * set to true when domain of object property is anonymous 
	 */
	public void setDomainAnonymous(boolean domainAnonymous) {
		this.domainAnonymous = domainAnonymous;
	}
	
	/**
	 * returns if the range of the object property is anonymous 
	 */
	public boolean isRangeAnonymous() {
		return rangeAnonymous;
	}

	/**
	 * set to true when range of object property is anonymous 
	 */
	public void setRangeAnonymous(boolean rangeAnonymous) {
		this.rangeAnonymous = rangeAnonymous;
	}
	
	/**
	 * returns the concepts of the domain (usually a single one) that are defined in the ontology 
	 */
	public HashSet<Concept> getDomainConcept() {
		return domainConcept;
	}

	/**
	 * set the concepts of the domain that are defined in the ontology  
	 */
	public void setDomainConcept(HashSet<Concept> domainConcept) {
		this.domainConcept = domainConcept;
	}

	/**
	 * returns the concepts of the range (usually a single one) that are defined in the ontology 
	 */
	public HashSet<Concept> getRangeConcept() {
		return rangeConcept;
	}

	/**
	 * set the concepts of the range that are defined in the ontology  
	 */
	public void setRangeConcept(HashSet<Concept> rangeConcept) {
		this.rangeConcept = rangeConcept;
	}
	
	/**
	 * if a verb occurs in the label of the object property the base form is returned, null otherwise
	 */
	public String getVerbBaseForm() {
		return verbBaseForm;
	}

	/**
	 * set the verb base form for this object property
	 */
	public void setVerbBaseForm(String verbBaseForm) {
		this.verbBaseForm = verbBaseForm;
	}

	/**
	 * returns "kind of" an intersection between the domain label and the object property label or null
	 */
	@Deprecated
	public Label getDomainLabelX() {
		return domainLabelX;
	}

	@Deprecated
	public void setDomainLabelX(Label domainLabel) {
		this.domainLabelX = domainLabel;
	}
	
	/**
	 * returns "kind of" an intersection between the range label and the object property label or null
	 */
	@Deprecated
	public Label getRangeLabelX() {
		return rangeLabelX;
	}
	
	@Deprecated
	public void setRangeLabelX(Label rangeLabel) {
		this.rangeLabelX = rangeLabel;
	}
	
	
	public String toInfoString(){
		String s = "ObjectProperty: " + this.labels.iterator().next().toSpacedString() + "\nVerbbase: " + this.verbBaseForm;
		if(domainLabelX!=null){
			s = s + "\nDomainLabel: " + domainLabelX.toSpacedString();
		}
		if(rangeLabelX!=null){
			s = s + "\nRangeLabel: " + rangeLabelX.toSpacedString();
		}
		return s;
	}
	
	/**
	 * returns either null or a domain label that occured in the object property
	 */
	public Label getDomainLabel() {
		return domainLabel;
	}

	public void setDomainLabel(Label logicalDomainLabel) {
		this.domainLabel = logicalDomainLabel;
	}

	/**
	 * returns either null or a range label that occured in the object property
	 */
	public Label getRangeLabel() {
		return rangeLabel;
	}

	public void setRangeLabel(Label logicalRangeLabel) {
		this.rangeLabel = logicalRangeLabel;
	}

	public Concept getConceptOfDomainLabel() {
		return conceptOfDomainLabel;
	}

	public void setConceptOfDomainLabel(Concept conceptToDomainLabel) {
		this.conceptOfDomainLabel = conceptToDomainLabel;
	}

	public Concept getConceptOfRangeLabel() {
		return conceptOfRangeLabel;
	}

	public void setConceptOfRangeLabel(Concept conceptToRangeLabel) {
		this.conceptOfRangeLabel = conceptToRangeLabel;
	}

	

}
