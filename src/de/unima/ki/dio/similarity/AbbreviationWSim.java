package de.unima.ki.dio.similarity;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.Word;

public class AbbreviationWSim implements WordSimilarity {

	/**
	 * Returns the similarity based on the Levensthein distance. If the distance is below a threshold defined in the settings
	 * then the method return 0. This method is intended to be used for detecting typos and spelling variants.
	 */
	public double getSimilarity(Word w1, Word w2) {
		return this.getSimilarity(w1.getToken(), w2.getToken());
	}
	
	
	/**
	 * Returns the similarity based on the Levensthein distance. If the distance is below a threshold defined in the settings
	 * then the method return 0. This method is intended to be used for detecting typos and spelling variants.
	 */
	public double getSimilarity(String w1, String w2) {
		if (w1.length() == 1 || w2.length() == 1) {
			if (w1.toLowerCase().charAt(0) == w2.toLowerCase().charAt(0)) {
				return 1.0 - Settings.MALUS_FOR_ABBREVIATION_HIT;
			}
		}
		return 0.0;

	}
	
	
	

}
