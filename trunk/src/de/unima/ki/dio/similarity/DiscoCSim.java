package de.unima.ki.dio.similarity;

import java.io.IOException;

import de.linguatools.disco.Compositionality;
import de.linguatools.disco.DISCO;
import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.Word;

/**
* This class can be used for computing the similarity between multi words (and single words).
* 
*
*/
public class DiscoCSim implements WordSimilarity {


	private DISCO disco;
	private Compositionality discoM;
	
	
	public DiscoCSim() {
		try {
			this.disco = new DISCO(Settings.DISCO_DIRECTORY, false);
			this.discoM = new Compositionality();
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
				double ssim = (double)this.disco.secondOrderSimilarity(w1.toLowerCase(), w2.toLowerCase());
				//double dsim = this.discoM.compositionalSemanticSimilarity(w1, w2, Compositionality.VectorCompositionMethod.COMBINED, Compositionality.SimilarityMeasures.KOLB, disco, null, null, null, null);
				double dsim = this.discoM.compositionalSemanticSimilarity(w1, w2, Compositionality.VectorCompositionMethod.MULTIPLICATION, Compositionality.SimilarityMeasures.KOLB, disco, null, null, null, null);
				double sim = Math.max(dsim, ssim);
				// System.out.println("sim: " + sim + " dsim: " + dsim);
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
		DiscoCSim dcsim = new DiscoCSim();
		
		dcsim.getSimilarity("conference participant", "");
		dcsim.getSimilarity("conference paper", "article");
		dcsim.getSimilarity("article", "paper");
		
		
	}
	
	


}
