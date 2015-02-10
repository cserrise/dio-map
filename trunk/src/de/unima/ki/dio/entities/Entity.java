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
	
	public void addLabel(Label label) {
		this.labels.add(label);
		
		
	}
	
	public Label getPreferedLabel() {
		return this.preferedLabel;
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

	public void resetLabel(Label label) {
		this.labels.clear();
		this.labels.add(label);
		this.preferedLabel = label;
		label.addEntity(this);
		
	}
	
	/**
	 * Resets all labels, while keeping the previously stored preferred label.
	 */
	public void resetLabels() {
		this.labels.clear();

	}

}
