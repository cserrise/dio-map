package de.unima.ki.dio.entities;

import java.util.ArrayList;
import java.util.HashSet;


public class LabelConverter {
	
	private static String[] stopwords = new String[] {
		"has", "have",
		"is", "been",
		"a", "an", "the"
	};
	
	
	public static void overwriteBySimplifiedLabel(Entity e, Label label) {
		ArrayList<Word> words = new ArrayList<Word>();
		
		for (int i = 0; i < label.getNumberOfWords(); i++) {
			Word w = label.getWord(i);
			boolean isStopword = false;
			for (String stopword : stopwords) {
				if (w.getToken().equals(stopword)) {
					isStopword = true;
					break;
				}
			}
			if (!isStopword) {
				words.add(w);
			}
		}
		
		if (e instanceof ObjectProperty) {
			ObjectProperty oprop = (ObjectProperty)e;
			HashSet<Concept>domains = oprop.getDomainConcept();
			removeDomainLabels(words, domains);
		}
		if (e instanceof ObjectProperty) {
			ObjectProperty oprop = (ObjectProperty)e;
			HashSet<Concept>ranges = oprop.getRangeConcept();
			removeRangeLabels(words, ranges);
		}
		if (e instanceof DataProperty) {
			DataProperty dprop = (DataProperty)e;
			HashSet<Concept>domains = dprop.getDomainConcept();
			removeDomainLabels(words, domains);
		}
		
		Label simplified = new Label(words);
		simplified.setRawString(label.toRawString());
		/*
		if (simplified.getNumberOfWords() != label.getNumberOfWords()) {
			System.out.println("--> original label " + label);
			System.out.println("--> simplified label " + simplified);			
		}
		*/
		e.resetLabel(simplified);		
	}


	/**
	* Removes the words used for describing  the concepts that are the domains from a list of words if the appear at the beginning.
	* Implemented for single and two word labels.
	* 
	* @param words
	* @param domains
	*/
	private static void removeDomainLabels(ArrayList<Word> words, HashSet<Concept> domains) {
		if (domains.size() == 1) {
			Concept domain = domains.iterator().next();
			if (words.size() >= 1 && words.get(0).getToken().equals(domain.preferedLabel.toRawString())) {
				words.remove(0);
			}
			else if (words.size() >= 2 && (words.get(0).getToken() + words.get(1).getToken()).equals(domain.preferedLabel.toRawString())) {
				words.remove(0);
				words.remove(0);
			}
		}
	}
	
	/**
	* Removes the words used for describing  the concepts that are the range from a list of words if the appear at the end.
	* Implemented for single and two word labels.
	* 
	* @param words
	* @param domains
	*/
	private static void removeRangeLabels(ArrayList<Word> words, HashSet<Concept> ranges) {
		if (ranges.size() == 1) {
			Concept range = ranges.iterator().next();
			if (words.size() >= 1 && words.get(words.size()-1).getToken().equals(range.preferedLabel.toRawString())) {
				words.remove(words.size()-1);
			}
			else if (words.size() >= 2 && (words.get(words.size()-1).getToken() + words.get(words.size()-2).getToken()).equals(range.preferedLabel.toRawString())) {
				words.remove(words.size()-1);
				words.remove(words.size()-1);
			}
		}
	}
	
	
	
	

}
