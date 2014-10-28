package de.unima.ki.dio;

public class Settings {
	
	// used to split labels from ontologies 
	public static final String REGEX_FOR_SPLIT = "(?<=\\p{Ll})(?=\\p{Lu})|(?<=\\p{L})(?=\\p{Lu}\\p{Ll})|(_)";
	
	public static final String DISCO_DIRECTORY = "res/enwiki-20130403-sim-lemma-mwl-lc/";

	// everything above 0.6 does not give anymore
	public static final double DISCO_THRESHOLD = 0.6;
	
	public static final double LEVENSHTEIN_THRESHOLD = 0.85;
	
	public static final String ROCKIT_ENDPOINT = "xxxx";
	
	public static final String TEMP_DIR = "tmp/";

	public static final String OWL_NS = "http://www.w3.org/2002/07/owl";
	
	
	
}
