package de.unima.ki.dio.experiments;


import de.unima.ki.dio.entities.Ontology;
import de.unima.ki.dio.exceptions.AlignmentException;
import de.unima.ki.dio.exceptions.DioException;
import de.unima.ki.dio.matcher.MarkovMatcher;
import de.unima.ki.dio.matcher.Matcher;
import de.unima.ki.dio.matcher.SimpleMatcher;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.filter.Greedy11;

public class TestMarkov {

	
	private static String ontPath = "exp/minitest1/";
	private static String refXPath = "exp/minitest1/references/";
	
	public static void main(String[] args) throws DioException {
		

		String ont1Id = "ont2";
		String ont2Id = "ont1"; 
		runTestcase(ont1Id, ont2Id);

		
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
		
		
		System.out.println("Markov Alignment:");
		System.out.println(markovAli);
		System.out.println("Simple Alignment:");
		System.out.println(simpleAli11);		
		
	}

}
