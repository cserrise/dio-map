package de.unima.ki.dio.entities;

import java.util.HashSet;

public class DataProperty extends Entity {
	
	protected HashSet<Label> domainLabels = new HashSet<Label>();

	public DataProperty(String uri, Label label) {
		super(uri, label);
	}
	
	
	public void addDomainLabel(Label label) {
		this.domainLabels.add(label);
	}
	
	
	

}
