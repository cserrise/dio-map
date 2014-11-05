package de.unima.ki.dio.similarity;

import de.unima.ki.dio.entities.Word;

public interface WordSimilarity {
	
	public double getSimilarity(Word w1, Word w2);

	public double getSimilarity(String w1, String w2);
	
}
