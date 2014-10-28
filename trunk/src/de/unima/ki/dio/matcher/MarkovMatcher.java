package de.unima.ki.dio.matcher;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.Concept;
import de.unima.ki.dio.entities.Entity;
import de.unima.ki.dio.entities.Label;
import de.unima.ki.dio.entities.Ontology;
import de.unima.ki.dio.entities.Word;
import de.unima.ki.dio.exceptions.RockitException;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.similarity.DiscoWSim;
import de.unima.ki.dio.similarity.WordSimilarity;



/**
 * 
 * UNDER CONSTRUCTION
 *
 */
public class MarkovMatcher implements Matcher {

	private Ontology ont1;
	private Ontology ont2;
	
	private PrintWriter writer;
	
	public Alignment match(Ontology ont1, Ontology ont2) throws RockitException {
		
		this.ont1 = ont1;
		this.ont2 = ont2;
		
		
		try {
			writer = new PrintWriter(Settings.TEMP_DIR + "evidence.db", "UTF-8");
		}
		catch (FileNotFoundException e) {
			throw new RockitException(RockitException.IO_ERROR, "could not create rockit evidence file", e);
		}
		catch (UnsupportedEncodingException e) {
			throw new RockitException(RockitException.IO_ERROR, "problems with encoding", e);
		}
		createEvidence();
		runRockit();
		Alignment alignment = this.createAlignment();
		writer.close();
		return alignment;
	}
	
	private void createEvidence() {
		
		WordSimilarity discoWSim = new DiscoWSim();
	
		
		HashSet<Word> wordsOnt1 = ont1.getWords();
		HashSet<Word> wordsOnt2 = ont2.getWords();
		

		writeEvidenceWords(ont1.getWords(), "1");
		writeEvidenceWords(ont2.getWords(), "2");
		
		writeEvidenceEntities(ont1.getEntities(), "1");
		writeEvidenceEntities(ont2.getEntities(), "2");
	}

	private void writeEvidenceEntities(HashSet<Entity> entities, String ontId) {
		this.writeComment("entities in ontology " + ontId);
		for (Entity e : entities) {
			if (e instanceof Concept) {
				this.writeGroundAtom("concept" + ontId, e.getUri());
				
			}
			for (Label l : e.getLabels()) {
				int numOfWords = l.getNumberOfWords();
				
				ArrayList<String> params = new ArrayList<String>();
				params.add(e.getUri());
				params.addAll(l.getLMToken(ontId));
				this.writeGroundAtom("concept" + ontId + "hasLabel" + numOfWords, params.toArray(new String[params.size()]));
			}
		}
		this.writeln();
	}

	private void writeEvidenceWords(HashSet<Word> wordsOnt1, String ontId) {
		this.writeComment("words from labels of entities in ontology " + ontId);
		for (Word w1 : wordsOnt1) {
			this.writeGroundAtom("word1", w1.getMLId(ontId));	
		}
		this.writeln();
	}
	
	
	
	private void runRockit() {
		
		
		
	}
	
	private Alignment createAlignment() {
		
		return null;
	}
	
	private void writeGroundAtom(String predicate, String ... args) {
		String s = predicate;
		s += "(";
		for (int i = 0; i < args.length - 1; i++) {
			s += "\"" + args[i] + "\", ";
		}
		s += "\"" + args[args.length-1] + "\"";
		s += ")";
		writer.println(s);
	}
	
	private void writeComment(String comment) {
		writer.println("// " + comment);
	}
	
	private void writeln() {
		writer.println("");	
	}
	
	
	
}
