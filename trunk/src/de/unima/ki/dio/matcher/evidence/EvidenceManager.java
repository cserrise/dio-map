package de.unima.ki.dio.matcher.evidence;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.unima.ki.dio.Settings;

public class EvidenceManager {
	
	private static HashMap<String, ArrayList<String[]>> groundAtoms = new HashMap<String, ArrayList<String[]>>();
	private static HashMap<String, ArrayList<String[]>> groundAtomsWeighted = new HashMap<String, ArrayList<String[]>>();

	
	public static void clear() {
		groundAtoms.clear();
		groundAtomsWeighted.clear();
	}
	
	
	public static void write(PrintWriter writer) {		
		Set<String> predicates = groundAtoms.keySet();
		for (String predicate : predicates) {
			writer.println("// *** " + predicate + " **** ");
			ArrayList<String[]> argsList = groundAtoms.get(predicate);
			for (String[] args : argsList) {
				String line = getGroundAtom(predicate, args);
				writer.println(line);
			}
		}
		Set<String> predicatesWeighted = groundAtomsWeighted.keySet();
		for (String predicate : predicatesWeighted) {
			writer.println("// *** " + predicate + "(weighted) **** ");
			ArrayList<String[]> argsList = groundAtomsWeighted.get(predicate);
			for (String[] args : argsList) {
				String line = getGroundAtomWeighted(predicate, args);
				writer.println(line);
			}
		}
	}
	
	
	
	public static void addGroundAtom(String predicate, String ... args) {
		addGroundAtomsX(predicate, false, args);
	}
	
	
	public static void addGroundAtomWeighted(String predicate, String ... args) {
		addGroundAtomsX(predicate, true, args);	
	}
	
	
	// **************************
	// *** PRIVATE PLAYGROUND ***
	// **************************
	
	/**
	* Adds ground atoms and choses the correct data structure to store them depending on the boolean variable.
	* 
	* @param predicate The predicate of the atom.
	* @param weighted If true, adds the ground atoms as weighted ground atoms, otherwise as unweighted ground atoms.
	* @param args The arguments of the predicate.
	*/
	public static void addGroundAtomsX(String predicate, boolean weighted, String ... args) {
		HashMap<String, ArrayList<String[]>> atoms = weighted ? groundAtomsWeighted : groundAtoms;
		if (!atoms.containsKey(predicate)) {
			atoms.put(predicate, new ArrayList<String[]>());
		}
		
		String[] arguments = new String[args.length];
		for (int i = 0; i < args.length; i++) arguments[i] = args[i];
		atoms.get(predicate).add(arguments);	
	}
	
	
	private static String getGroundAtom(String predicate, String ... args) {
		String s = predicate;
		s += "(";
		for (int i = 0; i < args.length - 1; i++) {
			s += "\"" + args[i] + "\", ";
		}
		s += "\"" + args[args.length-1] + "\"";
		s += ")";
		return s;
	}
	
	
	
	private static String getGroundAtomWeighted(String predicate, String ... args) {
		String s = predicate;
		s += "(";
		for (int i = 0; i < args.length - 1; i++) {
			s += "\"" + args[i] + "\", ";
		}
		s += normalize(args[args.length-1]);
		s += ")";
		return s;
	}
	
	private static Set<String> getConstantsFromPredicate(String predicate, int index) {
		ArrayList<String[]> argsList;
		Set<String> constants = new HashSet<String>();
		if (groundAtoms.containsKey(predicate)) {
			 argsList = groundAtoms.get(predicate);
		}
		else if (groundAtomsWeighted.containsKey(predicate)) {
			argsList = groundAtomsWeighted.get(predicate);
		}
		else {
			return constants;
		}
		
		for (String[] args : argsList) {
			constants.add(args[index]);
		}
		return constants;
	}

	
	private static double normalize(String sim) {
		double d = Double.parseDouble(sim);
		return normalize(d);
	}

	public static double normalize(double sim) {
		double upperBound = Settings.SIM_UPPER_BOUND;
		double lowerBound = Settings.SIM_LOWER_BOUND;
		if (Settings.SIM_BOUND_HYPO) {
			upperBound = Settings.SIM_UPPER_BOUND_HYPO;
			lowerBound = Settings.SIM_LOWER_BOUND_HYPO;			
		}
		
		double span = upperBound - lowerBound;
		double nsim = sim * span;
		nsim += lowerBound;
		return nsim;
	}
	

	

}
