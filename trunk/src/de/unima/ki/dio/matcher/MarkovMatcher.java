package de.unima.ki.dio.matcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.*;
import de.unima.ki.dio.exceptions.RockitException;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Correspondence;
import de.unima.ki.dio.rockit.RemoteRockit;
import de.unima.ki.dio.rockit.RockitResult;
import de.unima.ki.dio.similarity.*;



/**
 * 
 * UNDER CONSTRUCTION
 *
 */
public class MarkovMatcher implements Matcher {

	private Ontology ont1;
	private Ontology ont2;
	
	private PrintWriter writer;
	private PrintWriter out;

	WordSimilarity discoWSim;
	WordSimilarity levenstheinWSim;
	
	public MarkovMatcher() {
		this.discoWSim = new DiscoWSim();
		this.levenstheinWSim = new LevenstheinWSim();
	}
	
	public Alignment match(Ontology ont1, Ontology ont2) throws RockitException {
		
		this.ont1 = ont1;
		this.ont2 = ont2;
		
		
		try {
			writer = new PrintWriter(Settings.ROCKIT_EVIDENCEFILEPATH, "UTF-8");
			out = new PrintWriter(Settings.ROCKIT_LOCALOUT, "UTF-8");
		}
		catch (FileNotFoundException e) {
			throw new RockitException(RockitException.IO_ERROR, "could not create rockit evidence file", e);
		}
		catch (UnsupportedEncodingException e) {
			throw new RockitException(RockitException.IO_ERROR, "problems with encoding", e);
		}
		createEvidence();
		writer.close();
		Alignment alignment = runRockit();
		out.close();
		return alignment;
	}
	
	private void createEvidence() {
		writeEvidenceEntities(ont1.getEntities(), "1");
		writeEvidenceEntities(ont2.getEntities(), "2");		
		writeEvidenceWordSimilarity(ont1.getWords(), "1", ont2.getWords(), "2");
	}

	private void writeEvidenceEntities(HashSet<Entity> entities, String ontId) {
		this.writeComment("entities in ontology " + ontId);
		for (Entity e : entities) {
			
			/*
			if (e instanceof Concept) {
				this.writeGroundAtom("concept" + ontId, e.getUri());
				
			}
			*/
			for (Label l : e.getLabels()) {
				int numOfWords = l.getNumberOfWords();
				
				ArrayList<String> params = new ArrayList<String>();
				params.add(e.getUri());
				params.addAll(l.getLMToken(ontId));
				if (numOfWords < 3) {
					this.writeGroundAtom("concept" + ontId + "hasLabel" + numOfWords, params.toArray(new String[params.size()]));
				}
			}
		}
		this.writeln();
	}

	
	private void writeEvidenceWordSimilarity(HashSet<Word> wordsOnt1, String ont1Id, HashSet<Word> wordsOnt2, String ont2Id) {
		this.writeComment("words similarities");
		for (Word w1 : wordsOnt1) {
			for (Word w2 : wordsOnt2) {
				double lsim = this.levenstheinWSim.getSimilarity(w1, w2);
				double dsim = this.discoWSim.getSimilarity(w1, w2);
				double sim = Math.max(lsim, dsim);
				if (sim > 0) {
					this.writeGroundAtomWeighted("wordSim", w1.getMLId(ont1Id), w2.getMLId(ont2Id), "" + sim);	
				}
				else {
					this.writeGroundAtom("!wordEquiv", w1.getMLId(ont1Id), w2.getMLId(ont2Id));	
					
				}
			}
		}
		
		this.writeln();
	}
	
	
	private Alignment runRockit() throws RockitException {
		Alignment alignment = new Alignment();
		RemoteRockit rockit = new RemoteRockit(Settings.ROCKIT_MODELFILEPATH, Settings.ROCKIT_EVIDENCEFILEPATH);
		RockitResult rr = rockit.run();
		
		ArrayList<String[]> ce = rr.getAtomsOfPredicate("conceptEquiv");
		System.out.println("Markov Matcher Objective: " + rr.getObjective());
		for (String[] values : ce) {
			Correspondence c = new Correspondence(values[0], values[1]);
			alignment.add(c);
		}
		
		for (String[] values : ce) {
			out.println("conceptEquiv: " + values[0] + ", " +values[1]);
		}
		ArrayList<String[]> we = rr.getAtomsOfPredicate("wordEquiv");
		for (String[] values : we) {
			out.println("wordEquiv: " + values[0] + ", " +values[1]);
		}
		
		ArrayList<String[]> b1 = rr.getAtomsOfPredicate("blind1");
		ArrayList<String[]> b2 = rr.getAtomsOfPredicate("blind2");
		for (String[] values : b1) {
			out.println("blind1: " + values[0]);
		}
		for (String[] values : b2) {
			out.println("blind2: " + values[0]);
		}
		
		return alignment;
	}
	
	private void writeGroundAtom(String predicate, String ... args) {
		String s = predicate;
		s += "(";
		for (int i = 0; i < args.length - 1; i++) {
			// s += args[i] + ", ";
			s += "\"" + args[i] + "\", ";
		}
		
		s += "\"" + args[args.length-1] + "\"";
		// s += args[args.length-1];
		s += ")";
		writer.println(s);
	}
	
	
	private void writeGroundAtomWeighted(String predicate, String ... args) {
		String s = predicate;
		s += "(";
		for (int i = 0; i < args.length - 1; i++) {
			// s += args[i] + ", ";
			s += "\"" + args[i] + "\", ";
		}
		
		// s += "\"" + args[args.length-1] + "\"";
		s += args[args.length-1];
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
