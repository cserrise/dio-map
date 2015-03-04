package de.unima.ki.dio.matcher.evidence;

import java.util.ArrayList;

public class Clause {

	ArrayList<String> literals;
	
	public Clause() {
		this.literals = new ArrayList<String>();
		
	}
	
	public void addLiteral(String predicate, String ... args) {
		EvidenceManager.getGroundAtom(predicate, args);
		this.literals.add(EvidenceManager.getGroundAtom(predicate, args));
		
	}
	
	public String toString() {
		if (this.literals.size() == 0) {
			return "";
		}
		String s = "";
		for (int i = 0; i < literals.size() - 1; i++) {
			s += literals.get(i) + " v ";
		}
		s += literals.get(literals.size() -1) + ".";
		return s;
	}
}
