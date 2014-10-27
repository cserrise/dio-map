package de.unima.ki.dio;

public class Settings {
	
	//Used to split labels from ontologies 
	public static final String REGEX_FOR_SPLIT = "(?<=\\p{Ll})(?=\\p{Lu})|(?<=\\p{L})(?=\\p{Lu}\\p{Ll})|(_)";
	
	public static final String DISCO_DIRECTORY = "res/enwiki-20130403-sim-lemma-mwl-lc/";

	public static final double DISCO_THRESHOLD = 0.3;
	
	public static final double LEVENSHTEIN_THRESHOLD = 0.85;
	
	public static final String ROCKIT_ENDPOINT = "xxxx";
	
	
	
}
