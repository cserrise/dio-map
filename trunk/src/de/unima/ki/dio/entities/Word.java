package de.unima.ki.dio.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Word {

	private WordType type;	
	private String token;
	
	private static HashMap<String, Word> store = new HashMap<String, Word>();
	
	private HashSet<Label> labels;

	/**
	 * Constructs a word that is a wrapper for a string.
	 * 
	 * @param token The string.
	 */
	private Word(String token) {
		this.token = token;
		this.type = WordType.UNKNOWN;
		this.labels = new HashSet<Label>();
	}
	
	/**
	 * Adds a label in which this word appeared.
	 * 
	 * @param label The label.
	 */
	public void addLabel(Label label) {
		labels.add(label);
	}
	
	/**
	 * 
	 * @return The set of labels in which this word appeared.
	 */
	public Set<Label> getLabels() {
		return this.labels;
	}
	
	/**
	 * Sets the type of the word in terms of its part of speech role.
	 * 
	 * @param type The type of the word (part of speech).
	 */
	public void setWordType(WordType type) {
		this.type = type;
	}
	

	/**
	* 
	* @return The probable type of the word.
	*/
	public WordType getType() {
		return this.type;
	}
	
	/**
	 *  
	 * @return The string for which this word is a wrapper.
	 */
	public String getToken() {
		return this.token;
	}
	
	/**
	 * Two words are equal if the are wrappers for the same string. 
	 */
	public boolean equals(Object word) {
		if (!(word instanceof Word)) {
			return false;
		}
		else {
			Word wordAsWord = (Word)word;
			if (this.token.equals(wordAsWord.getToken())) {
				return true;
			}
			else {
				return false;
			}
			
		}
	}
	
	public static void resetStore() {
		Word.store.clear();
	}
	
	public static Word createWord(String token) {
		Word w;
		if (Word.store.containsKey(token)) {
			w = Word.store.get(token);
		}
		else {
			w = new Word(token);
			Word.store.put(token, w);
		}
		return w;
	}
	
	public static Word createWord(String token, WordType type) {
		Word w = createWord(token);
		w.setWordType(type);
		return w;
	}
	
	public static void printStore() {
		Set<String> keys = Word.store.keySet();
		for (String key : keys) {
			Word w = Word.store.get(key);
			System.out.println(w);
			for (Label l : w.labels) {
				System.out.println("\tLabel: " + l);
				for (Entity c : l.getEntities()) {
					System.out.println("\tConcept: " + c);
					
				}
			}
			
		}
		
		
		
	}
	
	
	
	public String toString() {
		return this.token;
	}
	
	
}
