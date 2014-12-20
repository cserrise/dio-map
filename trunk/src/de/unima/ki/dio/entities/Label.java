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
	
	/**
	* Builds the raw string, which is the simple concatenation of words, and returns it.
	* 
	* @return The raw string.
	*/
	public String toRawString() {
		String rep = "";
		for (int i = 0; i < this.getNumberOfWords(); i++) {
			rep += this.getWord(i);  
			
		}
		return rep;
	}
	
	public String toSpacedString() {
		if(this.words.size() == 0) return "";
		String ret = this.getWord(0).toString();
		for (int i = 1; i < this.getNumberOfWords(); i++) {
			ret = ret + " " + this.getWord(i);  
			
		}
		return ret;
	}
	
	
	public String getMLLabel(String ontId) {
		return "L" + ontId + "#" + this.toRawString();
	}
	
	public ArrayList<String> getMLWords(String ontId) {
		ArrayList<String> tokens = new ArrayList<String>();
		for (int i = 0; i < this.words.size(); i++) {
			tokens.add(this.words.get(i).getMLId(ontId));
			
		}
		return tokens;
	}


}
