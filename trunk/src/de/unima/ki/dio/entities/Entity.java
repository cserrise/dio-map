package de.unima.ki.dio.entities;

import java.util.HashSet;

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

	public int getMaxNumOfWords() {
		int max = 0;
		for (Label l : this.labels) {
		
			max = (max < l.getNumberOfWords()) ? l.getNumberOfWords() : max;
		}
		return max;
	}

}
