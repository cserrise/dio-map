package de.unima.ki.dio.entities;

import java.util.HashSet;
import java.util.Set;

public abstract class Entity {
	
	protected Label preferedLabel;
	protected String uri;
	
	protected HashSet<Label> labels = new HashSet<Label>();

	
	public Entity(String uri, Label label) {
		this.uri = uri;
		this.preferedLabel = label;
		label.addEntity(this);
		this.labels.add(label);
		
	}

	public String toString() {
		return uri + " =>" + preferedLabel;
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public HashSet<Label> getLabels() {
		return this.labels;
		
	}

}
