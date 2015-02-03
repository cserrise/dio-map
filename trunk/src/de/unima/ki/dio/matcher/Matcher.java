package de.unima.ki.dio.matcher;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.Ontology;
import de.unima.ki.dio.exceptions.DioException;
import de.unima.ki.dio.matcher.alignment.Alignment;

public abstract class Matcher {
	
	public abstract Alignment match(Ontology ont1, Ontology ont2) throws DioException;
	
	protected double normalize(double sim) {
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
