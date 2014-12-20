package de.unima.ki.dio.entities;

import java.util.HashSet;

public class ObjectProperty extends Entity {
	
	private HashSet<Concept> domainConcept = new HashSet<Concept>();
	private HashSet<Concept> rangeConcept = new HashSet<Concept>();
	
	/**
	* If false, there is only one element in the domainConcept set.
	*/
	private boolean domainAnonymous = false;
	
	/**
	* If false, there is only one element in the rangeConcept set.
	*/
	private boolean rangeAnonymous = false;
	
	private String verbBaseForm = null;
	private Label domainLabel = null;
	private Label rangeLabel = null;
	
	private Label logicalDomainLabel = null;
	private Label logicalRangeLabel = null;
	
	public ObjectProperty(String uri, Label label) {
		super(uri, label);
	}

	public boolean isDomainAnonymous() {
		return domainAnonymous;
	}

	public void setDomainAnonymous(boolean domainAnonymous) {
		this.domainAnonymous = domainAnonymous;
	}

	public boolean isRangeAnonymous() {
		return rangeAnonymous;
	}

	public void setRangeAnonymous(boolean rangeAnonymous) {
		this.rangeAnonymous = rangeAnonymous;
	}

	public HashSet<Concept> getDomainConcept() {
		return domainConcept;
	}

	public void setDomainConcept(HashSet<Concept> domainConcept) {
		this.domainConcept = domainConcept;
	}

	public HashSet<Concept> getRangeConcept() {
		return rangeConcept;
	}

	public void setRangeConcept(HashSet<Concept> rangeConcept) {
		this.rangeConcept = rangeConcept;
	}

	public String getVerbBaseForm() {
		return verbBaseForm;
	}

	public void setVerbBaseForm(String verbBaseForm) {
		this.verbBaseForm = verbBaseForm;
	}

	public Label getDomainLabel() {
		return domainLabel;
	}

	public void setDomainLabel(Label domainLabel) {
		this.domainLabel = domainLabel;
	}

	public Label getRangeLabel() {
		return rangeLabel;
	}

	public void setRangeLabel(Label rangeLabel) {
		this.rangeLabel = rangeLabel;
	}
	
	
	public String toInfoString(){
		String s = "ObjectProperty: " + this.labels.iterator().next().toSpacedString() + "\nVerbbase: " + this.verbBaseForm;
		if(domainLabel!=null){
			s = s + "\nDomainLabel: " + domainLabel.toSpacedString();
		}
		if(rangeLabel!=null){
			s = s + "\nRangeLabel: " + rangeLabel.toSpacedString();
		}
		return s;
	}

	public Label getLogicalDomainLabel() {
		return logicalDomainLabel;
	}

	public void setLogicalDomainLabel(Label logicalDomainLabel) {
		this.logicalDomainLabel = logicalDomainLabel;
	}

	public Label getLogicalRangeLabel() {
		return logicalRangeLabel;
	}

	public void setLogicalRangeLabel(Label logicalRangeLabel) {
		this.logicalRangeLabel = logicalRangeLabel;
	}
	

}
