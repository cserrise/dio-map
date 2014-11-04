package de.unima.ki.dio.experiments;


import de.unima.ki.dio.entities.Ontology;
import de.unima.ki.dio.exceptions.AlignmentException;
import de.unima.ki.dio.exceptions.DioException;
import de.unima.ki.dio.matcher.MarkovMatcher;
import de.unima.ki.dio.matcher.Matcher;
import de.unima.ki.dio.matcher.SimpleMatcher;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Characteristic;
import de.unima.ki.dio.matcher.filter.Greedy11;

public class ConferenceMarkov {

	
	private static String ontPath = "exp/conference/ontologies/";
	private static String refXPath = "exp/conference/references/";
	
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
				// System.out.println();
				// System.exit(1);
				
			}			
		}
		

		
		
	}

	private static void runTestcase(String ont1Id, String ont2Id) throws DioException, AlignmentException {
		System.out.print(ont1Id + "\t" + ont2Id + "\t");
		String ont1Path = ontPath + ont1Id + ".owl";
		String ont2Path = ontPath + ont2Id + ".owl";
		String refPath = refXPath + ont1Id + "-"  + ont2Id + ".rdf";
		
		Ontology ont1 = new Ontology(ont1Path);
		Ontology ont2 = new Ontology(ont2Path);
		
		Matcher markov = new MarkovMatcher();
		Matcher simple = new SimpleMatcher();
		
		Alignment markovAli = markov.match(ont1, ont2);
		Alignment simpleAli = simple.match(ont1, ont2);
		Alignment simpleAli11  = Greedy11.filter(simpleAli);
		
		Alignment reference = new Alignment(refPath);
		
	
		Characteristic markovC=  new Characteristic(markovAli, reference);
		Characteristic simpleC11=  new Characteristic(simpleAli11, reference);
		
		if  (markovC.getNumOfRulesCorrect() != simpleC11.getNumOfRulesCorrect() || markovC.getNumOfRulesMatcher() != simpleC11.getNumOfRulesMatcher()) {
			System.out.print(markovC.toShortDesc() + "\t" + simpleC11.toShortDesc());
			System.out.println("In markov not in simple:\n" + markovAli.minus(simpleAli));
			System.out.println("In simple not in markov:\n" + simpleAli.minus(markovAli));
		}
		else {
			System.out.print("same");
			
		}
		System.out.println();		
		markovAli.write("exp/results/markov-blind-plural60/" + ont1Id + "-" + ont2Id + ".rdf");


	}

}
