package de.unima.ki.dio.similarity;

import de.unima.ki.dio.entities.Label;
import de.unima.ki.dio.entities.Word;

public class DictionaryMock implements CompoundOracle, WordSimilarity {

	@Override
	public boolean isKnownCompound(Label l) {
		if (l.getNumberOfWords() == 2) {
			String w1 = l.getWord(0).getToken();
			String w2 = l.getWord(1).getToken();
			if (w1.toLowerCase().equals("subject") && w2.toLowerCase().equals("area")) {
				return true;
			}
			
			if (w1.toLowerCase().equals("last") && w2.toLowerCase().equals("name")) {
				return true;
			}
			
		}
		return false;
	}

	@Override
	public double getSimilarity(Word w1, Word w2) {
		return this.getSimilarity(w1.getToken(), w2.getToken());
	}

	@Override
	public double getSimilarity(String w1, String w2) {
		//System.out.println("W1: " + w1);
		//System.out.println("W2: " + w2);
		//System.out.println(" ------------ ");
		// in wordnet two steps
		if (w1.equals("subject area") && w2.equals("topic")) {
			return 0.6;
		}
		if (w2.equals("subject area") && w1.equals("topic")) {
			return 0.6;
		}
		//in wordnet 2 steps
		if (w1.equals("location") && w2.equals("place")) {
			return 0.6;
		}
		if (w2.equals("place") && w1.equals("location")) {
			return 0.6;
		}
		
		// in wordnet 1 step
		if (w1.equals("chairman") && w2.equals("chair")) {
			return 0.6;
		}
		if (w2.equals("chair") && w1.equals("chairman")) {
			return 0.6;
		}
		// in wordnet 2 steps
		if (w1.equals("institution") && w2.equals("organization")) {
			return 0.6;
		}
		if (w2.equals("institution") && w1.equals("organization")) {
			return 0.6;
		}
		if (w1.equals("institution") && w2.equals("organisation")) {
			return 0.6;
		}
		if (w2.equals("institution") && w1.equals("organisation")) {
			return 0.6;
		}
		if (w1.equals("institution") && w2.equals("organization")) {
			return 0.6;
		}
		if (w2.equals("institution") && w1.equals("organization")) {
			return 0.6;
		}
		// in wordnet by 1 step
		if (w1.equals("surname") && w2.equals("last name")) {
			return 0.6;
		}
		if (w2.equals("surname") && w1.equals("last name")) {
			return 0.6;
		}
		
		// in wordnet by 3 step
		if (w1.equals("address") && w2.equals("talk")) {
			return 0.55;
		}
		if (w2.equals("address") && w1.equals("talk")) {
			return 0.55;
		}
		
		// in wordnet by 3 step
		if (w1.equals("reviewed") && w2.equals("evaluated")) {
			return 0.55;
		}
		if (w2.equals("reviewed") && w1.equals("evaluated")) {
			return 0.55;
		}
		
		return 0;
	}

}
