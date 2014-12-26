package de.unima.ki.dio.experiments;


import de.unima.ki.dio.Settings;
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
	private static String outputPath = "exp/results/temp/";
	
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
		
		
		
		/*
		String[] ontIds = {
				"cmt",
				"conference"
		};
		*/
		
		
		String refp = Settings.ROCKIT_EVIDENCEFILEPATH;
		for (int i = 0; i < ontIds.length - 1; i++) {
			for (int j = i+1; j < ontIds.length; j++) {
				String ont1Id = ontIds[i];
				String ont2Id = ontIds[j]; 
				String testcaseId = ont1Id  + "-" + ont2Id; 
				//String ont1Id = "cmt";
				//String ont2Id = "edas"; 
				Settings.ROCKIT_EVIDENCEFILEPATH = refp + "-" + testcaseId;
				runTestcase(ont1Id, ont2Id);
				
				// System.exit(1);
				// System.out.println("Died due to System.exit()!");
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
		simpleAli.applyThreshold(0.0);
		Alignment simpleAli11  = Greedy11.filter(simpleAli);
		
		Alignment reference = new Alignment(refPath);
		
	
		Characteristic markovC=  new Characteristic(markovAli, reference);
		Characteristic simpleC11=  new Characteristic(simpleAli11, reference);
		
		if  (markovC.getNumOfRulesCorrect() != simpleC11.getNumOfRulesCorrect() || markovC.getNumOfRulesMatcher() != simpleC11.getNumOfRulesMatcher()) {
			System.out.print(markovC.toShortDesc() + "\t" + simpleC11.toShortDesc());
			System.out.println("");
			System.out.println("In markov not in simple11:\n" + markovAli.minus(simpleAli11));
			System.out.println("In simple11 not in markov:\n" + simpleAli11.minus(markovAli));
		}
		else {
			System.out.print("same");
			
		}
		System.out.println();		
		markovAli.write(outputPath + ont1Id + "-" + ont2Id + ".rdf");


	}

}
