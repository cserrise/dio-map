package de.unima.ki.dio.similarity;

import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.Word;

public class LevenstheinWSim implements WordSimilarity {

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
		// TODO implement this function, it should return the similaroity score if it above a threshold defined in the settings
		// and it
		Levenshtein lev = new Levenshtein();
		double val = (double)lev.getSimilarity(w1.toLowerCase(), w2.toLowerCase());
		double valS1 = (double)lev.getSimilarity(w1.toLowerCase() + "s", w2.toLowerCase());
		double valS2 = (double)lev.getSimilarity(w1.toLowerCase(), w2.toLowerCase() + "s");
		
		if (valS1 >= val || valS2 >= val) {
			val = Math.max(valS1, valS2) - Settings.MALUS_FOR_PLURALS_SIMILIARITY;
		}
		val = (val >= Settings.LEVENSHTEIN_THRESHOLD) ? val : 0.0;
		return val;
	}
	
	
	

}
