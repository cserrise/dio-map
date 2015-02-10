package de.unima.ki.dio.matcher;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.Ontology;
import de.unima.ki.dio.exceptions.DioException;
import de.unima.ki.dio.matcher.alignment.Alignment;

public abstract class Matcher {
	
	public abstract Alignment match(Ontology ont1, Ontology ont2) throws DioException;
	

	

}
