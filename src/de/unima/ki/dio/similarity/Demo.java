package de.unima.ki.dio.similarity;

import java.text.DecimalFormat;

public class Demo {
	
	public static void main(String[] args){
		CombinedCSim combined = new CombinedCSim();

		
		// String[] words = new String[]{"institution", "introduction", "contribution"};
		
		// String[] words = new String[]{"program commitee", "session chair", "subject area", "topic", "last name", "surname", "first name", "chair","reviewed", "participant","evaluated","paper","conference","session","submitted","accepted","fee","member","event","contribution"};
		// String[] words = new String[]{"participant", "attendee"};
		
		// String[] words = new String[]{"poster", "card", "institution", "organization", "chairman", "chair", "surname", "last name", "evaluated", "reviewed"};
		
		
		String[] words = new String[]{
				"poster", "card", "institution", "organization", "chairman", "chair", "surname", "last name", "evaluated", "reviewed",
				"part", "event", "subject area", "topic", "first name", "place", "location", "paper", "article",
				"session", "conference","submission", "document", "trip", "item", "person", "author", "committee","publisher",
				"participant", "attendee", "member", "listener", "administrator", "organizator", "paper", "contribution", "introduction", "excursion"
		};
		
		
		DecimalFormat df2 = new DecimalFormat( "0.000" );
		for (int i = 0 ; i < words.length; i++) {
			for (int j = 0 ; j < words.length; j++) {
				if (i == j) continue;
				String w1 = words[i];
				String w2 = words[j];
				
				double c = combined.getSimilarity(w1, w2);
				if (c > 0.5 && c < 1.0) {
					System.out.println(w1 + " = " + w2 + ": " + c);
				}
				

				
			}
			System.out.println(" - - - - - ");
		}
		
		
		
		
	}

}
