package de.unima.ki.dio.matcher;

import de.unima.ki.dio.entities.Word;

public class WordPair {

	private Word w1;
	private Word w2;
	private double score;
	
	public WordPair(Word w1, Word w2, double score) {
		this.w1 = w1;
		this.w2 = w2;
		this.score = score;
	}
	
	public String toString() {
		return this.w1.getToken() + " =? " +   this.w2.getToken() + " (" + this.score + ")";
		
	}
	
	
	
	

}
