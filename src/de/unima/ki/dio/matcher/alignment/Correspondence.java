package de.unima.ki.dio.matcher.alignment;

import java.util.HashMap;



public class Correspondence implements Comparable<Correspondence> {

	private String uri1;
	private String uri2;
	private double confidence;
	private SemanticRelation relation;
	
	private static HashMap<SemanticRelation, String> symbolTable = new HashMap<SemanticRelation, String>();
	
	static {
		symbolTable.put(SemanticRelation.EQUIV, "=");
		symbolTable.put(SemanticRelation.SUB, "<");
		symbolTable.put(SemanticRelation.SUPER, ">");
		symbolTable.put(SemanticRelation.DISJOINT, "!=");
		symbolTable.put(SemanticRelation.NA, "?");
	}
	
	public Correspondence(String uri1, String uri2, SemanticRelation relation, double confidence) {
		this.uri1 = uri1;
		this.uri2 = uri2;
		this.relation = relation;
		this.confidence = confidence;
	}
	
	public Correspondence(String uri1, String uri2, SemanticRelation relation) {
		this(uri1, uri2, relation, 1.0d);
	}

	public Correspondence(String uri1, String uri2, double confidence) {
		this(uri1, uri2, SemanticRelation.EQUIV, confidence);
	}
	
	public Correspondence(String uri1, String uri2) {
		this(uri1, uri2, SemanticRelation.EQUIV, 1.0d);
	}
	
	public String getUri1() {
		return this.uri1;
	}
	
	public String getUri2() {
		return this.uri2;
	}
	
	public double getConfidence() {
		return this.confidence;
	}
	
	public SemanticRelation getRelation() {
		return this.relation;
	}
	
	public String getRelationSymbol() {
		return Correspondence.symbolTable.get(this.relation);
	}
	
	
	
	
	
	public String toString() {
		String relationSymbol = Correspondence.symbolTable.get(this.relation);
		return uri1 + " " + relationSymbol + " " + uri2 + ", " + this.confidence;
	}


	public int compareTo(Correspondence correspondence) {
		Correspondence that = (Correspondence)correspondence;
		if (this.getConfidence() > that.getConfidence()) { return 1; }	
		else if (this.getConfidence() < that.getConfidence()) { return -1; }
		else {
			if (this.getUri1().compareTo(that.getUri1()) > 0) { return 1; }	
			else if	(this.getUri1().compareTo(that.getUri1()) < 0) { return -1; }	
			else {
				if (this.getUri2().compareTo(that.getUri2()) > 0) { return 1; }	
				else if	(this.getUri2().compareTo(that.getUri2()) < 0) { return -1; }	
				else { return 0; }
			}
		}
	}	
	
	
	
	

}