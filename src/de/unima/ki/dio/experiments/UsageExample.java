package de.unima.ki.dio.experiments;


import de.unima.ki.dio.entities.Concept;
import de.unima.ki.dio.entities.Label;
import de.unima.ki.dio.entities.Ontology;
import de.unima.ki.dio.entities.Word;
import de.unima.ki.dio.entities.WordType;
import de.unima.ki.dio.exceptions.AlignmentException;
import de.unima.ki.dio.matcher.SimpleMatcher;
import de.unima.ki.dio.matcher.alignment.Alignment;

public class UsageExample {


	public static void main(String[] args) throws AlignmentException {
		
		Ontology o1 = new Ontology(); 
		
		// Person
		//   ConferenceMember
		//     Speaker
		//   ConferenceAuthor
		// Document
		//   Paper	
		//     AcceptedPaper
		// ConferenceFee
		
		Label label;
		Concept concept;

		label = new Label(Word.createWord("Person", WordType.NOUN));
		concept = new Concept("http://o1#Person", label);
		o1.addConcept(concept);
		
		label = new Label(Word.createWord("Conference", WordType.NOUN), Word.createWord("Member", WordType.NOUN));
		concept = new Concept("http://o1#ConferenceMember", label);
		o1.addConcept(concept);
		
		label = new Label(Word.createWord("Speaker", WordType.NOUN));
		concept = new Concept("http://o1#Speaker", label);
		o1.addConcept(concept);
		
		label = new Label(Word.createWord("Conference", WordType.NOUN), Word.createWord("Author", WordType.NOUN));
		concept = new Concept("http://o1#ConferenceAuthor", label);
		o1.addConcept(concept);
		
		label = new Label(Word.createWord("Document", WordType.NOUN));
		concept = new Concept("http://o1#Document", label);
		o1.addConcept(concept);
		
		label = new Label(Word.createWord("Paper", WordType.NOUN));
		concept = new Concept("http://o1#Paper", label);
		o1.addConcept(concept);
		
		label = new Label(Word.createWord("Accepted", WordType.ADJECTIVE), Word.createWord("Paper", WordType.NOUN));
		concept = new Concept("http://o1#AcceptedPaper", label);
		o1.addConcept(concept);
		
		label = new Label(Word.createWord("Conference", WordType.NOUN), Word.createWord("Fee", WordType.NOUN));
		concept = new Concept("http://o1#ConferenceFee", label);
		o1.addConcept(concept);
		
		System.out.println("Ontology 1: \n" + o1);
		
		// ***********************************************
		
		Ontology o2 = new Ontology(); 
		
		// Person
		//   ConferenceParticpant
		//   Author
		//     FirstAuthor
		// Document
		//   Article	
		//     AcceptedArticle
	
		label = new Label(Word.createWord("Person", WordType.NOUN));
		concept = new Concept("http://o2#Person", label);
		o2.addConcept(concept);
		
		label = new Label(Word.createWord("Conference", WordType.NOUN), Word.createWord("Participant", WordType.NOUN));
		concept = new Concept("http://o2#ConferenceParticipant", label);
		o2.addConcept(concept);
		
		label = new Label(Word.createWord("Author", WordType.NOUN));
		concept = new Concept("http://o2#Author", label);
		o2.addConcept(concept);
		
		label = new Label(Word.createWord("First", WordType.NOUN), Word.createWord("Author", WordType.NOUN));
		concept = new Concept("http://o2#FirstAuthor", label);
		o2.addConcept(concept);
		
		label = new Label(Word.createWord("Document", WordType.NOUN));
		concept = new Concept("http://o2#Document", label);
		o2.addConcept(concept);
		
		label = new Label(Word.createWord("Article", WordType.NOUN));
		concept = new Concept("http://o2#Article", label);
		o2.addConcept(concept);
		
		label = new Label(Word.createWord("Accepted", WordType.ADJECTIVE), Word.createWord("Article", WordType.NOUN));
		concept = new Concept("http://o2#AcceptedArticle", label);
		o2.addConcept(concept);
		
		
		

		System.out.println("Ontology 2: \n" + o2);
		
		SimpleMatcher sm = new SimpleMatcher();
		Alignment alignment = sm.match(o1, o2);
		
		alignment.write("exp/out.txt");
		System.out.println(alignment);
		

		
	}

}
