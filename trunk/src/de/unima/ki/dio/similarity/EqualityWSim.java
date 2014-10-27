package de.unima.ki.dio.similarity;

import de.unima.ki.dio.entities.Word;

public class EqualityWSim implements WordSimilarity {

	
	public double getSimilarity(Word w1, Word w2) {
		if (w1.getToken().toLowerCase().equals(w2.getToken().toLowerCase())) {
			return 1.0d;
		}
		else {
			return 0.0d;
		}
	}


}
