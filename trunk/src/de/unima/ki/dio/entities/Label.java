package de.unima.ki.dio.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Label {
	
	private ArrayList<Word> words; 
	private HashSet<Entity> entities;
	private String rawString = null;
	
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
	
	public void setRawString(String rawString) {
		this.rawString = rawString;
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
		if (rawString == null) {
			String rep = "";
			for (int i = 0; i < this.getNumberOfWords(); i++) {
				rep += this.getWord(i) + this.getWord(i).getSuffix();  
				
			}
			return rep;
		}
		else {
			return this.rawString;
			
		}
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

	/**
	* Checks if the tokens of this and the given labels are the same. 
	* 
	* @param label The label that is compared with this label.
	* @return True if equal, false otherwise.
	*/
	public boolean hasEqualTokens(Label label) {
		if (label.words.size() != this.words.size()) {
			return false;
		}
		for (int i = 0; i < this.words.size(); i++) {
			if (!this.words.get(i).getToken().equals(label.words.get(i).getToken())) {
				return  false;
			}
		}
		return true;
	}
	
	public boolean equals(Object that) {
		if (!(that instanceof Label)) {
			return false;
		}
		else {
			Label thatLabel = (Label)that;
			return this.toString().equals(thatLabel.toString());
		}
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}
	


}
