package de.unima.ki.dio.similarity;

import de.unima.ki.dio.entities.Word;

public class EqualityWSim implements WordSimilarity {

	
	public double getSimilarity(Word w1, Word w2) {
		return this.getSimilarity(w1.getToken(), w2.getToken());
	}
	
	public double getSimilarity(String w1, String w2) {
		if (w1.toLowerCase().equals(w2.toLowerCase())) {
			return 1.0d;
		}
		else {
			return 0.0d;
		}
	}


}
