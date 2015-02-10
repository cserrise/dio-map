package de.unima.ki.dio;

public class Settings {
	
	/**
	 * If set to true, a local installation of rockIt is used. Otherwise the web services
	 * will be called. 
	 */
	public static final boolean USE_LOCAL_ROCKIT = true;
	
	// used to split labels from ontologies 
	// remove forst or to not support detection of abbreviations
	public static final String REGEX_FOR_SPLIT = "(?<=\\p{Lu})(?=\\p{Lu})|(?<=\\p{Ll})(?=\\p{Lu})|(?<=\\p{L})(?=\\p{Lu}\\p{Ll})|(_)";
	
	
	public static final String DISCO_DIRECTORY_BNC = "res/en-BNC-20080721/";
	public static final String DISCO_DIRECTORY_WIKI = "res/enwiki-20130403-sim-lemma-mwl-lc/";
	
	
	// public static final String DISCO_DIRECTORY = "res/enwiki-20130403-sim-lemma-mwl-lc/";
	// is the following one the really learge one?
	public static String DISCO_DIRECTORY = DISCO_DIRECTORY_WIKI;
	
	// the MOST IMPORTANT params are insed the model
	public static final String ROCKIT_MODELFILEPATH            = "mod/mod_simple_strict.ml";
	public static final String ROCKIT_MODELFILEPATH_HYPOTHESIS = "mod/mod_simple_strict_reversed.ml";
	// public static final String ROCKIT_MODELFILEPATH = "mod/model_beta2.ml";
	
	// similarity thresholds
	public static double DISCO_THRESHOLD = -0.1;
	public static double DISCO_THRESHOLD_PROPERTIES = -0.1;
	public static final double LEVENSHTEIN_THRESHOLD = 0.85;
	
	// [-1.3 ... 0.7]
	// span for similarities above the threshold
	// first for the pre-computation of the hypothesis
	public static double SIM_LOWER_BOUND_HYPO = 0.0;
	public static double SIM_UPPER_BOUND_HYPO = 1.0;
	
	// then for the computation of the final alignment
	public static double SIM_LOWER_BOUND = -1.0;
	public static double SIM_UPPER_BOUND = 0.0;
	
	// 
	public static boolean SIM_BOUND_HYPO = true;

	
	
	public static final String TEMP_DIR = "tmp/";

	public static final String OWL_NS = "http://www.w3.org/2002/07/owl";
	
	
	// settings relavant for rockit
	public static final String ROCKIT_ENDPOINT = "xxxx";
	public static final double ROCKIT_GAP = 0.0000000001; 
	
	public static String ROCKIT_EVIDENCEFILEPATH =  TEMP_DIR + "evidence.db";
	public static final String ROCKIT_LOCALOUT =  TEMP_DIR + "out.db";

	public static final double MALUS_FOR_PLURALS_SIMILIARITY = 0.01;
	public static final double MALUS_FOR_ABBREVIATION_HIT = 0.3;

	public static final int MAX_NUM_OF_WORDS_IN_LABEL = 3;

	public static boolean ENSURE_COHERENCY = true;
	
	public static final String SUFFIX = "xyxyx";

	
	public static final int MAX_NUM_OF_SIMILARITIES_CONCEPT = 15;
	public static final int MAX_NUM_OF_SIMILARITIES_PROP = 15;

	
	
}
