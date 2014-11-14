package de.unima.ki.dio.similarity;

import java.io.IOException;

import de.linguatools.disco.Compositionality;
import de.linguatools.disco.DISCO;
import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.Word;

public class DiscoWSim implements WordSimilarity {


	private DISCO disco;
	
	public DiscoWSim() {
		try {
			this.disco = new DISCO(Settings.DISCO_DIRECTORY, false);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.err.println("problem initializing DISCO, check filepath for the files that provide thet stats");
			System.exit(1);
		}
		
	}
	
	public double getSimilarity(Word w1, Word w2) {
		return getSimilarity(w1.getToken(), w2.getToken());
		
	}
	
	public double getSimilarity(String w1, String w2) {
		try {
			if (w1.equalsIgnoreCase(w2)) {
				return 1.0d;
			}
			else {
				double sim = (double)this.disco.secondOrderSimilarity(w1.toLowerCase(), w2.toLowerCase());
				if (sim < Settings.DISCO_THRESHOLD) {
					return 0.0d;
				}
				else  {
					return sim;			
				}	
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return 0.0d;
		
	}
	
	public static void main(String[] args) {		
		try {
			DISCO disco = new DISCO(Settings.DISCO_DIRECTORY, false);
			Compositionality discoC = new Compositionality();
			String s1 = "conference paper";
			String s2 = "article";
			double csim = discoC.compositionalSemanticSimilarity(s1, s2, Compositionality.VectorCompositionMethod.COMBINED, Compositionality.SimilarityMeasures.KOLB, disco, null, null, null, null);
			System.out.println(s1 + " <-> " + s2 + ": " + csim);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	


}
