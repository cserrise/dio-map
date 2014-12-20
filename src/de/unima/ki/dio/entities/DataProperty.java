package de.unima.ki.dio.entities;

import java.util.HashSet;

public class DataProperty extends Entity {
	
	private HashSet<Concept> domainConcept = new HashSet<Concept>();
	
	private String verbBaseForm = null;
	private Datatype datatype = null;
	private Label logicalRangeLabel = null;
	
	private boolean domainAnonymous = false;

	public DataProperty(String uri, Label label) {
		super(uri, label);
	}

	public HashSet<Concept> getDomainConcept() {
		return domainConcept;
	}

	public void setDomainConcept(HashSet<Concept> domainConcept) {
		this.domainConcept = domainConcept;
	}

	public boolean isDomainAnonymous() {
		return domainAnonymous;
	}

	public void setDomainAnonymous(boolean domainAnonymous) {
		this.domainAnonymous = domainAnonymous;
	}

	public String getVerbBaseForm() {
		return verbBaseForm;
	}

	public void setVerbBaseForm(String verbBaseForm) {
		this.verbBaseForm = verbBaseForm;
	}

	public Datatype getDatatype() {
		return datatype;
	}

	public void setDatatype(Datatype datatype) {
		this.datatype = datatype;
	}

	public Label getLogicalRangeLabel() {
		return logicalRangeLabel;
	}

	public void setLogicalRangeLabel(Label logicalRangeLabel) {
		this.logicalRangeLabel = logicalRangeLabel;
	}
	
	
	
	
	

}
