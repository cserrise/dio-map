package de.unima.ki.dio.entities;

import java.util.ArrayList;
import java.util.HashSet;

import de.unima.ki.dio.similarity.CompoundOracle;
import de.unima.ki.dio.similarity.DictCSim;
import de.unima.ki.dio.similarity.DictionaryMock;


public class LabelExtender {
	
	private static String[] stopwords = new String[] {
		"has", "have",
		"is", "been",
		"a", "an", "the",
		"of", "by", "in",
		"to"
	};
	
	private static String[] dataPropertyStopwords = new String[] {
		"date"
	};
	
	
	private static CompoundOracle co = new DictCSim();
	
	public static void addLabelsForInverseProperties(Entity e1, Entity e2) {
		//System.out.println("INVERSE (before extension):");
		//System.out.println("e1 = " + e1.getUri());
		//System.out.println("e2 = " + e2.getUri());
		HashSet<Label> allLabels = new HashSet<Label>();
		for (Label l  : e1.getLabels()) {
			// System.out.println("... label of e1 = " + l.toString());
			allLabels.add(l);
		}
		for (Label l  : e2.getLabels()) {
			//System.out.println("... label of e2 = " + l.toString());
			allLabels.add(l);
		}
		e1.resetLabels();
		e2.resetLabels();
		for (Label l : allLabels) {
			e1.addLabel(l);
			e2.addLabel(l);
		}
	}
	
	public static void expandLabelsOfCompound(Entity e) {
		HashSet<Label> additionalLabels = new HashSet<Label>();
		// create new labels

		for (Label label  : e.getLabels()) {
			if (co.isKnownCompound(label)) {
				String compoundToken = label.getWord(0).getToken();
				for (int i = 1; i < label.getNumberOfWords(); i++) {
					compoundToken += " " + label.getWord(i);
				}
				String prefix = "C";
				if (e instanceof DataProperty) prefix = "D";
				if (e instanceof ObjectProperty) prefix = "O";
				Word w = Word.createWord(prefix, compoundToken, WordType.UNKNOWN);
				//System.out.println("Created and added compound: " + w);
				
				Label labelCompound = new Label(w);
				additionalLabels.add(labelCompound);
				

			}
		}
		// now add the new labels
		for (Label additionalLabel : additionalLabels) {
			additionalLabel.addEntity(e);
			e.addLabel(additionalLabel);
		}
		
	}
	
	public static void addSimplifiedLabel(Entity e) {
		Label label = e.getPreferedLabel();
		ArrayList<Word> words = new ArrayList<Word>();
		
		for (int i = 0; i < label.getNumberOfWords(); i++) {
			Word w = label.getWord(i);
			boolean isStopword = false;
			for (String stopword : stopwords) {
				if (w.getToken().toLowerCase().equals(stopword)) {
					isStopword = true;
					break;
				}
			}
			if (e instanceof DataProperty) {
				for (String stopword : dataPropertyStopwords) {
					if (w.getToken().toLowerCase().equals(stopword)) {
						isStopword = true;
						break;
					}
				}
			}
			if (!isStopword) {
				words.add(w);
			}
		}
		// replace the label by the simplified label
		if (words.size() < label.getNumberOfWords()) {
			Label simplified = new Label(words);
			simplified.setRawString(label.toRawString());
			e.resetLabel(simplified);		
		}

		if (e instanceof ObjectProperty) {
			ObjectProperty oprop = (ObjectProperty)e;
			HashSet<Concept>domains = oprop.getDomainConcept();
			if (removeDomainLabels(words, domains)) {
				Label modified = new Label(words);
				e.addLabel(modified);
			}
		}
		if (e instanceof ObjectProperty) {
			ObjectProperty oprop = (ObjectProperty)e;
			HashSet<Concept>ranges = oprop.getRangeConcept();
			if (removeRangeLabels(words, ranges)) {
				Label modified = new Label(words);
				e.addLabel(modified);
			}
		}
		if (e instanceof DataProperty) {
			DataProperty dprop = (DataProperty)e;
			HashSet<Concept>domains = dprop.getDomainConcept();
			if (removeDomainLabels(words, domains)) {
				Label modified = new Label(words);
				e.addLabel(modified);
			}
		}
		
		//System.out.println("AFTER SIMPLIFICATION:");
		//System.out.println("e = " + e.getUri());
		//HashSet<Label> allLabels = new HashSet<Label>();
		//for (Label l  : e.getLabels()) {
		//	System.out.println("... label of e = " + l.toString());
		//	allLabels.add(l);
		//}


		
	}


	/**
	* Removes the words used for describing  the concepts that are the domains from a list of words if the appear at the beginning.
	* Implemented for single and two word labels.
	* 
	* @param words
	* @param domains
	*/
	private static boolean removeDomainLabels(ArrayList<Word> words, HashSet<Concept> domains) {
		boolean modified = false;
		if (domains.size() == 1) {
			Concept domain = domains.iterator().next();
			if (words.size() >= 1 && words.get(0).getToken().equals(domain.preferedLabel.toRawString())) {
				words.remove(0);
				modified = true;
			}
			else if (words.size() >= 2 && (words.get(0).getToken() + words.get(1).getToken()).equals(domain.preferedLabel.toRawString())) {
				words.remove(0);
				words.remove(0);
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	* Removes the words used for describing  the concepts that are the range from a list of words if the appear at the end.
	* Implemented for single and two word labels.
	* 
	* @param words
	* @param domains
	*/
	private static boolean removeRangeLabels(ArrayList<Word> words, HashSet<Concept> ranges) {
		boolean modified = false;
		if (ranges.size() == 1) {
			Concept range = ranges.iterator().next();
			if (words.size() >= 1 && words.get(words.size()-1).getToken().equals(range.preferedLabel.toRawString())) {
				words.remove(words.size()-1);
				modified = true;
			}
			else if (words.size() >= 2 && (words.get(words.size()-1).getToken() + words.get(words.size()-2).getToken()).equals(range.preferedLabel.toRawString())) {
				words.remove(words.size()-1);
				words.remove(words.size()-1);
				modified = true;
			}
		}
		return modified;
	}
	
	
	
	

}
