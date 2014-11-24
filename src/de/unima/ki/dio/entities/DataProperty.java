package de.unima.ki.dio.entities;

import java.util.HashSet;

public class DataProperty extends Entity {
	
	private HashSet<Concept> domainConcept = new HashSet<Concept>();
	
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
	
	
	
	
	

}
