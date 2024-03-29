package de.unima.ki.dio.matcher;


import de.unima.ki.dio.entities.*;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Correspondence;
import de.unima.ki.dio.matcher.evidence.EvidenceManager;
import de.unima.ki.dio.similarity.*;

/**
 * A classical simple matcher that uses Equivality, Levensthein (with very high threshold) to detect spelling mistakes or variants and 
 * DISCO to detect that different words have the same meaning. It only matches entities on other entities if their labels have the same
 * length and each of the words has a similar counterpart. The resulting confidence is the product of all involved similarities. 
 * 
 * The resulting mapping is then filtered with a greedy 1:1 approach.
 *
 */
public class SimpleMatcher extends Matcher {

	public Alignment match(Ontology ont1, Ontology ont2) {
		
		WordSimilarity discoWSim = new DiscoWSim();
		WordSimilarity lshtein = new LevenstheinWSim();
		
		Correspondence c;
		Alignment ali = new Alignment();
		
		for (Entity e1 : ont1.getEntities()) {
			for (Entity e2 : ont2.getEntities()) {
				double bestSim = -100.0;
				for (Label l1 : e1.getLabels()) {
					for (Label l2 : e2.getLabels()) {
						if (l1.getNumberOfWords() == l2.getNumberOfWords()) {
							double currentSim = 1.0;
							double lsim = 0.0;
							double dsim = 0.0;
							for (int i = 0; i < l1.getNumberOfWords(); i++) {
								lsim = lshtein.getSimilarity(l1.getWord(i), l2.getWord(i));
								// lsim = 0.0d;
								dsim = discoWSim.getSimilarity(l1.getWord(i), l2.getWord(i));
								currentSim *= Math.max(lsim, dsim);
							}

							if (currentSim > bestSim) {
								bestSim = currentSim;
							}
						}
						
					}				
					
				}
				if (bestSim > -100.0) {
					bestSim = EvidenceManager.normalize(bestSim);
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
