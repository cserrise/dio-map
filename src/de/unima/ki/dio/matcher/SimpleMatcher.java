package de.unima.ki.dio.matcher;

import java.util.HashSet;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.*;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Correspondence;
import de.unima.ki.dio.matcher.filter.Greedy11;
import de.unima.ki.dio.similarity.*;

/**
 * A classical simple matcher that uses Equivality, Levensthein (with very high threshold) to detect spelling mistakes or variants and 
 * DISCO to detect that different words have the same meaning. It only matches entities on other entities if their labels have the same
 * length and each of the words has a similar counterpart. The resulting confidence is the product of all involved similarities. 
 * 
 * The resulting mapping is then filtered with a greedy 1:1 approach.
 *
 */
public class SimpleMatcher implements Matcher {

	public Alignment match(Ontology ont1, Ontology ont2) {
		
		WordSimilarity discoWSim = new DiscoWSim();
		WordSimilarity lshtein = new LevenstheinWSim();
		
		Correspondence c;
		Alignment ali = new Alignment();
		
		for (Entity e1 : ont1.getEntities()) {
			for (Entity e2 : ont2.getEntities()) {
				double bestSim = 0.0;
				for (Label l1 : e1.getLabels()) {
					for (Label l2 : e2.getLabels()) {
						if (l1.getNumberOfWords() == l2.getNumberOfWords()) {
							double currentSim = 1.0;
							for (int i = 0; i < l1.getNumberOfWords(); i++) {
								currentSim *= lshtein.getSimilarity(l1.getWord(i), l2.getWord(i));
//								System.out.println(l1.getWord(i) + "  " + l2.getWord(i) + "  " + currentSim);
							}
							if(currentSim < Settings.LEVENSHTEIN_THRESHOLD){
								currentSim = 1.0;
								for (int i = 0; i < l1.getNumberOfWords(); i++) {								
									currentSim *= discoWSim.getSimilarity(l1.getWord(i), l2.getWord(i));
								}
							}

							if (currentSim > bestSim) {
								bestSim = currentSim;
							}
						}
						
					}				
					
				}
				if (bestSim > 0) {
					c = new Correspondence(e1.getUri(), e2.getUri(), bestSim);
					ali.add(c);
				}			
			}			
			
		}

		Alignment ali11 = Greedy11.filter(ali);
		
		return ali11;
	}
	
	

	
}
