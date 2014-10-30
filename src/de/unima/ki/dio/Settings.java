package de.unima.ki.dio;

public class Settings {
	
	// used to split labels from ontologies 
	public static final String REGEX_FOR_SPLIT = "(?<=\\p{Ll})(?=\\p{Lu})|(?<=\\p{L})(?=\\p{Lu}\\p{Ll})|(_)";
	
	public static final String DISCO_DIRECTORY = "res/enwiki-20130403-sim-lemma-mwl-lc/";

	// similarity thresholds
	public static final double DISCO_THRESHOLD = 0.8;
	public static final double LEVENSHTEIN_THRESHOLD = 0.85;
	
	public static final String TEMP_DIR = "tmp/";

	public static final String OWL_NS = "http://www.w3.org/2002/07/owl";
	
	
	// settings relavant for rockit
	public static final String ROCKIT_ENDPOINT = "xxxx";
	public static final double ROCKIT_GAP = 0.0000000001; 
	public static final String ROCKIT_MODELFILEPATH = "mod/model2.ml";
	public static final String ROCKIT_EVIDENCEFILEPATH =  TEMP_DIR + "evidence.db";
	public static final String ROCKIT_LOCALOUT =  TEMP_DIR + "out.db";
	
	
	
}
