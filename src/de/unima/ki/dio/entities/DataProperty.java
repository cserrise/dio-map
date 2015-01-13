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
	
	public static boolean isDatatypeCompatibleWithDatatype(Datatype datatype1, Datatype datatype2){
		//0 = Date, 1 = String, 2 = Int, 3 = Boolean, 4 = Double, 5 = Float, 6 = Long
		//SAME horizontally as vertically
		boolean[][] compatibilityMatrix = new boolean[][]{
			{true,  true,  false, false, false, false, false},
			{true,  true,  true,  false, true,  true,  true },
			{false, true,  true,  false, true,  true,  true },
			{false, false, false, true,  false, false, false},
			{false, true,  true,  false, true,  true,  true },
			{false, true,  true,  false, true,  true,  true },
			{false, true,  true,  false, true,  true,  true }
		};
		return compatibilityMatrix[datatype1.ordinal()][datatype2.ordinal()];
	}
	
	
	

}
