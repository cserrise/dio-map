package de.unima.ki.dio.similarity;

import de.unima.ki.dio.entities.Word;

public class LevenstheinWSim implements WordSimilarity {

	/**
	 * Returns the similarity based on the Levensthein distance. If the distance is below a threshold defined in the settings
	 * then the method return 0. This method is intended to be used for detecting typos and spelling variants.
	 */
	public double getSimilarity(Word w1, Word w2) {
		// TODO implement this function, it should return the similaroity score if it above a threshold defined in the settings
		// and it
		return 0;
	}
	
	

}
