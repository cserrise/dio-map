package de.unima.ki.dio.matcher;

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
public class MarkovMatcher extends Matcher {

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
		// createGroundedRules();
		writer.close();
		Alignment alignment = runRockit();
		out.close();
		return alignment;
	}
	
	private void createGroundedRules() {
		writeGroundedRulesLCEQ(ont1.getEntities(), "1", ont2.getEntities(), "2");
	}
	
	
	private void writeGroundedRulesLCEQ(HashSet<Entity> entities1, String ont1Id, HashSet<Entity> entities2, String ont2Id) {
		this.writeComment("grounded rules to relate concepts with their labels via existential quantification");
		
		for (Entity e1 : entities1) {
			for (Entity e2 : entities2) {
				ArrayList<String> atoms = new ArrayList<String>();
				atoms.add(groundAtom("!conceptEQ", new String[]{e1.getUri(), e2.getUri()}));
				for (Label l1 : e1.getLabels()) {
					for (Label l2 : e2.getLabels()) {
						atoms.add(groundAtom("labelEQ", new String[]{l1.getMLLabel(ont1Id), l2.getMLLabel(ont2Id)}));
					}	
				}
				String d = getDisjunction(atoms);
				writer.write(d + "\n");
			}
		}
		this.writeln();
	}
	

	private String getDisjunction(ArrayList<String> atoms) {
		String dis = "";
		for (int i = 0; i < atoms.size() -1; i++) {
			dis += atoms.get(i) + " v ";
		}
		dis += atoms.get(atoms.size() -1);
		return dis;
	}

	private void createEvidence() {
		writeEvidenceEntities(ont1.getEntities(), "1");
		writeEvidenceEntities(ont2.getEntities(), "2");		
		writeEvidenceSemantics(ont1.getEntities(), "1");
		writeEvidenceSemantics(ont2.getEntities(), "2");
		writeEvidenceWordSimilarity(ont1.getWords(), "1", ont2.getWords(), "2");
		
	}

	private void writeEvidenceEntities(HashSet<Entity> entities, String ontId) {
		this.writeComment("entities in ontology " + ontId);
		for (Entity e : entities) {
			for (Label l : e.getLabels()) {
				int numOfWords = l.getNumberOfWords();
				if (numOfWords <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
					ArrayList<String> paramsC2L = new ArrayList<String>();
					paramsC2L.add(e.getUri());
					paramsC2L.add(l.getMLLabel(ontId));
					this.writelnGroundAtom("hasLabel_o" + ontId, paramsC2L.toArray(new String[paramsC2L.size()]));
					ArrayList<String> paramsL2W = new ArrayList<String>();
					paramsL2W.add(l.getMLLabel(ontId));
					paramsL2W.addAll(l.getMLWords(ontId));
					this.writelnGroundAtom("has" + numOfWords + "Word_o" + ontId , paramsL2W.toArray(new String[paramsL2W.size()]));
				}
			}
		}
		this.writeln();
	}
	
	private void writeEvidenceSemantics(HashSet<Entity> entities, String ontId) {
		this.writeComment("subsumption and disjointness between concepts in ontology " + ontId);
		for (Entity e1 : entities) {
			if (e1 instanceof Concept) {
				for (Entity e2 : entities) {
					if (e2 instanceof Concept) {
						Concept e1Concept = (Concept)e1;
						Concept e2Concept = (Concept)e2;
						// System.out.println(e1 + "   MAX NUM OF WORDS = " + e1.getMaxNumOfWords());
						// System.out.println(e2 + "   MAX NUM OF WORDS = " + e2.getMaxNumOfWords());
						if (e1.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL &&  e2.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
							if (e1Concept.isSubClass(e2Concept)) {
								this.writelnGroundAtom("sub_o" + ontId, e1.getUri(), e2.getUri());
							}
							if (e1Concept.isDisjoint(e2Concept)) {
								this.writelnGroundAtom("dis_o" + ontId, e1.getUri(), e2.getUri());
							}
						}
					}
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
					this.writelnGroundAtomWeighted("wordSim", w1.getMLId(ont1Id), w2.getMLId(ont2Id), "" + normalize(sim));	
				}
				else {
					this.writelnGroundAtom("!wordEQ", w1.getMLId(ont1Id), w2.getMLId(ont2Id));	
					
				}
			}
		}
		
		this.writeln();
	}

	
	
	
	private Alignment runRockit() throws RockitException {
		Alignment alignment = new Alignment();
		RemoteRockit rockit = new RemoteRockit(Settings.ROCKIT_MODELFILEPATH, Settings.ROCKIT_EVIDENCEFILEPATH);
		RockitResult rr = rockit.run();
		
		ArrayList<String[]> ce = rr.getAtomsOfPredicate("conceptEQ");
		
		System.out.println("Markov Matcher Objective: " + rr.getObjective());
		for (String[] values : ce) {
			Correspondence c = new Correspondence(values[0], values[1]);
			alignment.add(c);
		}
		
		for (String[] values : ce) {
			out.println("conceptEQ: " + values[0] + ", " +values[1]);
		}
		ArrayList<String[]> le = rr.getAtomsOfPredicate("labelEQ");
		for (String[] values : le) {
			out.println("labelEQ: " + values[0] + ", " +values[1]);
		}
		ArrayList<String[]> we = rr.getAtomsOfPredicate("wordEQ");
		for (String[] values : we) {
			out.println("wordEQ: " + values[0] + ", " +values[1]);
		}

		
		ArrayList<String[]> b1 = rr.getAtomsOfPredicate("blindWord_o1");
		ArrayList<String[]> b2 = rr.getAtomsOfPredicate("blindWord_o2");
		for (String[] values : b1) {
			out.println("blindWord_o1: " + values[0]);
		}
		for (String[] values : b2) {
			out.println("blindWord_o2: " + values[0]);
		}
		
		return alignment;
	}
	
	

	
	private void writelnGroundAtom(String predicate, String ... args) {
		String atom = groundAtom(predicate, args);
		writer.println(atom);
	}
	
	private String groundAtom(String predicate, String ... args) {
		String s = predicate;
		s += "(";
		for (int i = 0; i < args.length - 1; i++) {
			s += "\"" + args[i] + "\", ";
		}
		s += "\"" + args[args.length-1] + "\"";
		s += ")";
		return s;
	}
	
	
	
	
	private void writelnGroundAtomWeighted(String predicate, String ... args) {
		String atom = groundAtomWeighted(predicate, args);
		writer.println(atom);
	}

	
	private String groundAtomWeighted(String predicate, String ... args) {
		String s = predicate;
		s += "(";
		for (int i = 0; i < args.length - 1; i++) {
			s += "\"" + args[i] + "\", ";
		}
		s += args[args.length-1];
		s += ")";
		return s;
	}
	
	private void writeComment(String comment) {
		writer.println("// " + comment);
	}
	
	private void writeln() {
		writer.println("");	
	}
	
	
	
}