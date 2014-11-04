package de.unima.ki.dio.experiments;

import de.unima.ki.dio.entities.Ontology;
import de.unima.ki.dio.exceptions.AlignmentException;
import de.unima.ki.dio.exceptions.DioException;
import de.unima.ki.dio.matcher.Matcher;
import de.unima.ki.dio.matcher.SimpleMatcher;
import de.unima.ki.dio.matcher.StringMatcher;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Characteristic;
import de.unima.ki.dio.matcher.filter.Greedy11;

/**
 *  This class performs experiments to check the results of the simple matcher against the conference dataset.
 */
public class ConferenceSimple {
	
	public static String outPath = "exp/results/simple-noplural/";
	
	
	public static void main(String[] args) throws DioException {
		
		String[] ontIds = {
			"cmt",
			"conference",
			"confof",
			"edas",
			"ekaw",
			"iasted",
			"sigkdd"
		};
		
		for (int i = 0; i < ontIds.length - 1; i++) {
			for (int j = i+1; j < ontIds.length; j++) {
				String ont1Id = ontIds[i];
				String ont2Id = ontIds[j]; 
				runTestcase(ont1Id, ont2Id);
				
			}			
		}
		

		
		
	}

	private static void runTestcase(String ont1Id, String ont2Id) throws DioException, AlignmentException {
		System.out.print(ont1Id + "\t" + ont2Id + "\t");
		String ont1Path = "exp/conference/ontologies/" + ont1Id + ".owl";
		String ont2Path = "exp/conference/ontologies/" + ont2Id + ".owl";
		String refPath = "exp/conference/references/" + ont1Id + "-"  + ont2Id + ".rdf";
		
		Ontology ont1 = new Ontology(ont1Path);
		Ontology ont2 = new Ontology(ont2Path);
		
		Matcher matcher = new SimpleMatcher();
		Matcher matcherS = new StringMatcher();
		
		Alignment alignmentS = matcherS.match(ont1, ont2);
		Alignment alignment = matcher.match(ont1, ont2);
		Alignment alignment11 = Greedy11.filter(alignment);
		
		Alignment reference = new Alignment(refPath);
		
		Characteristic cS =  new Characteristic(alignmentS, reference);
		Characteristic c =  new Characteristic(alignment, reference);
		Characteristic c11 =  new Characteristic(alignment11, reference);
		
		alignmentS.write(outPath + ont1Id + "-" + ont2Id + ".rdf");
		
		int numOfAddCorrect = c.getNumOfRulesCorrect() - cS.getNumOfRulesCorrect();
		int numOfAddWrong = (c.getNumOfRulesMatcher() - cS.getNumOfRulesMatcher()) - numOfAddCorrect;
		
		System.out.println(cS.toShortDesc() + "\t" + c.toShortDesc() + "\t" +  c11.toShortDesc() + "\t" + numOfAddCorrect + "\t" + numOfAddWrong );
		
		

	}

}
