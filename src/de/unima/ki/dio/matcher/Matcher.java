package de.unima.ki.dio.matcher;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.Ontology;
import de.unima.ki.dio.exceptions.DioException;
import de.unima.ki.dio.matcher.alignment.Alignment;

public abstract class Matcher {
	
	public abstract Alignment match(Ontology ont1, Ontology ont2) throws DioException;
	
	protected double normalize(double sim) {
		double span = Settings.SIM_UPPER_BOUND - Settings.SIM_LOWER_BOUND;
		double nsim = sim * span;
		nsim += Settings.SIM_LOWER_BOUND;
		return nsim;
	}
	

}
