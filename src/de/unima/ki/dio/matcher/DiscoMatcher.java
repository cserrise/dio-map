package de.unima.ki.dio.matcher;


import de.unima.ki.dio.entities.*;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Correspondence;
import de.unima.ki.dio.similarity.*;

/**
 * A matcher that uses Equivality, Levensthein (with very high threshold) to detect spelling mistakes or variants and 
 * DISCO to detect that different words have the same meaning. It differs from the SimpleMatcher by always matching the
 * full label as a whole via DISCO instead of matching its parts.. 
 * 
 * The resulting mapping is then filtered with a greedy 1:1 approach.
 *
 */
public class DiscoMatcher extends Matcher {

	public Alignment match(Ontology ont1, Ontology ont2) {
		
		WordSimilarity discoCSim = new DiscoCSim();
		WordSimilarity lshtein = new LevenstheinWSim();
		
		Correspondence c;
		Alignment ali = new Alignment();
		
		for (Entity e1 : ont1.getEntities()) {
			for (Entity e2 : ont2.getEntities()) {
				double bestSim = -100.0;
				for (Label l1 : e1.getLabels()) {
					for (Label l2 : e2.getLabels()) {
						if (l1.getNumberOfWords() == l2.getNumberOfWords()) {
							
							double lsim = 0.0;
							double dsim = 0.0;
							String l1words = "";
							String l2words = "";
							for (int i = 0; i < l1.getNumberOfWords(); i++) {
								l1words += l1.getWord(i) + " ";
							}
							for (int i = 0; i < l2.getNumberOfWords(); i++) {
								l2words += l2.getWord(i) + " ";
							}
							l1words = l1words.trim();
							l2words = l2words.trim();
							lsim = lshtein.getSimilarity(l1words, l2words);
							dsim = discoCSim.getSimilarity(l1words, l2words);
							double currentSim = Math.max(lsim, dsim);
							if (currentSim > 0.1 && (l1.getNumberOfWords() > 1 || l2.getNumberOfWords() > 1) ) {
								System.out.println(l1words + " <-> " + l2words + " currentsim: " + currentSim);
							}
							
							

							if (currentSim > bestSim) {
								bestSim = currentSim;
							}
						}
						
					}				
					
				}
				if (bestSim > -100.0) {
					bestSim = normalize(bestSim);
					if (bestSim > 0.0) {
						c = new Correspondence(e1.getUri(), e2.getUri(), bestSim);
						ali.add(c);
					}
					
				}			
			}			
			
		}

	
		
		return ali;
	}
	
	

	
}
