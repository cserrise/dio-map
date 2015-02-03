package de.unima.ki.dio.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.unima.ki.dio.Settings;

public class Word {

	private WordType type;	
	private String token;
	private String suffix = "";
	private String prefix = "";
	
	private static HashMap<String, Word> store = new HashMap<String, Word>();
	
	private HashSet<Label> labels;

	/**
	 * Constructs a word that is a wrapper for a string.
	 * 
	 * @param token The string.
	 */
	private Word(String prefix, String token) {
		this.prefix = prefix;
		this.token = token.toLowerCase();
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
	 * Sets the suffix from empty string to some special string.
	 * The suffix is used in the special case where exactly the same normalized label is used for several entities. 
	 * 
	 * @param id the id that is appended to the suffix.
	 */
	public void setSuffix(int id) {
		this.suffix = Settings.SUFFIX + id;
	}
	
	/**
	* The suffix is used in the special case where exactly the same normalized label is used for several entities. 
	* 
	* @return The suffix or an empty string.
	*/
	public String getSuffix() {
		return this.suffix;
	}
	
	/**
	* The prefix is used to mark the type of entity for which this word has been used. C = Concept, D = Dateproperty, O = Objectproperty
	* 
	* @return The suffix or an empty string.
	*/
	public String getPrefix() {
		return this.prefix;
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
			if ((this.token + this.suffix).equals(wordAsWord.getToken() + wordAsWord.getSuffix())) {
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
	
	public static Word createWord(String prefix, String token, String suffix) {
		Word w;
		if (Word.store.containsKey(prefix + token + suffix)) {
			w = Word.store.get(prefix + token + suffix);
		}
		else {
			w = new Word(prefix, token);
			Word.store.put(prefix + token + suffix, w);
		}
		return w;
	}
	
	/**
	 * Creates a new word without checking in the store if it already exists.
	 *  
	 * @param token
	 * @param id
	 * @return
	 */
	public static Word createNewWord(String prefix, String token, int id) {
		Word w = createWord(prefix, token, Settings.SUFFIX + id);
		w.setSuffix(id);
		return w;
	}
	
	
	public static Word createWord(String prefix, String token, WordType type) {
		if (type == WordType.NOUN_PLURAL && token.endsWith("s")) {
			token = token.substring(0, token.length()-1);
		}
		Word w = createWord(prefix, token, "");
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
	
	
	/**
	 *  
	 * @return The string for which this word is a wrapper.
	 */
	public String getMLId(String ontId) {
		return "W" + ontId + this.prefix + "#" + this.token + this.suffix;
	}
	
	
	public String toString() {
		return this.token;
	}
	
	
}
