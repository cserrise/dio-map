package de.unima.ki.dio.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Label {
	
	private ArrayList<Word> words; 
	private HashSet<Entity> entities;
	
	public Label(Word ... words) {
		this.words = new ArrayList<Word>();
		for (Word word : words) {
			this.words.add(word);
			word.addLabel(this);
		}	
		this.entities = new HashSet<Entity>();
	}
	
	public Label(ArrayList<Word> words) {
		this.words = new ArrayList<Word>();
		for (Word word : words) {
			this.words.add(word);
			word.addLabel(this);
		}	
		this.entities = new HashSet<Entity>();
	}
	
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public Set<Entity> getEntities() {
		return this.entities;
	}
	
	public int getNumberOfWords() {
		return this.words.size();
	}
	
	public Word getWord(int index) {
		return words.get(index);
	}
	
	public String toString() {
		String rep = "";
		for (int i = 0; i < this.getNumberOfWords(); i++) {
			rep += " (" + (i+1) + ") " + this.getWord(i);  
			
		}
		return rep;
	}


}
