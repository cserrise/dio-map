package de.unima.ki.dio.matcher;

import de.unima.ki.dio.entities.Ontology;
import de.unima.ki.dio.matcher.alignment.Alignment;

public interface Matcher {
	
	public Alignment match(Ontology ont1, Ontology ont2);
	

}
