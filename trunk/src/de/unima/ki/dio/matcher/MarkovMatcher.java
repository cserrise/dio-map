package de.unima.ki.dio.matcher;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;


import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.*;
import de.unima.ki.dio.exceptions.RockitException;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Correspondence;
import de.unima.ki.dio.matcher.evidence.EvidenceManager;
import de.unima.ki.dio.rockit.RockitAdapter;
import de.unima.ki.dio.rockit.RockitResult;
import de.unima.ki.dio.rockit.remote.RemoteRockit;
import de.unima.ki.dio.similarity.*;

public class MarkovMatcher extends Matcher {
	
	private PrintWriter writer;
	private PrintWriter out;

	WordSimilarity discoWSim;
	WordSimilarity abbreviationWSim;
	WordSimilarity levenstheinWSim;
	WordSimilarity dictionaryWSim;
	
	Ontology ont1;
	Ontology ont2;
	
	// public HashMap<String, String>
	
	public MarkovMatcher() {
		this.discoWSim = new DiscoWSim();
		this.abbreviationWSim = new AbbreviationWSim();
		this.levenstheinWSim = new LevenstheinWSim();
		this.dictionaryWSim = new DictionaryMock();
	}
	
	public Alignment match(Ontology ont1, Ontology ont2) throws RockitException {
		this.ont1 = ont1;
		this.ont2 = ont2;
		Alignment hypothesis = computeHypotheses();
		//System.out.println("Hypotheses that have been computed:");
		//System.out.println(hypothesis);
		Alignment alignment = computeAlignment(hypothesis);
		return alignment;
	}
	

	public Alignment computeHypotheses() throws RockitException {
		EvidenceManager.clear();
		System.out.println(">>> Computing hypotheses that are a superset of the final alignment <<<");
		Settings.SIM_BOUND_HYPO = true;
		try {
			writer = new PrintWriter(Settings.ROCKIT_EVIDENCEFILEPATH + "-HYPO", "UTF-8");
			out = new PrintWriter(Settings.ROCKIT_LOCALOUT+ "-HYPO", "UTF-8");
		}
		catch (FileNotFoundException e) {
			throw new RockitException(RockitException.IO_ERROR, "could not create rockit evidence file", e);
		}
		catch (UnsupportedEncodingException e) {
			throw new RockitException(RockitException.IO_ERROR, "problems with encoding", e);
		}

		extendLabels(ont1.getEntities());
		extendLabels(ont2.getEntities());
		
		writeEvidenceWordSimilarity("1", "2");
		writeEvidenceEntities(ont1.getEntities(), "1");
		writeEvidenceEntities(ont2.getEntities(), "2");	
		EvidenceManager.write(writer);
		writer.close();
		RockitAdapter rockitAdapter = new RockitAdapter(Settings.ROCKIT_MODELFILEPATH_HYPOTHESIS, Settings.ROCKIT_EVIDENCEFILEPATH + "-HYPO", Settings.ROCKIT_LOCALOUT + "-HYPO");
		Alignment alignment = rockitAdapter.runRockit();
		
		// out.close();
		return alignment;	
	}
	
	public Alignment computeAlignment(Alignment hypothesis) throws RockitException {
		Settings.SIM_BOUND_HYPO = false;
		System.out.println(">>> Computing final alignment <<< ");
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
		writeEvidenceHypothesis(ont1.getEntities(), ont2.getEntities(), hypothesis);	
		if (Settings.ENSURE_COHERENCY) {
			writeEvidenceSemantics(ont1.getEntities(), "1");
			writeEvidenceSemantics(ont2.getEntities(), "2");
		}
		EvidenceManager.write(writer);
		writer.close();
		RockitAdapter rockitAdapter = new RockitAdapter(Settings.ROCKIT_MODELFILEPATH, Settings.ROCKIT_EVIDENCEFILEPATH, Settings.ROCKIT_LOCALOUT);
		Alignment alignment = rockitAdapter.runRockit();
		
		out.close();
		return alignment;
	}
	
	
	private void extendLabels(HashSet<Entity> entities) {
		
		// modify and add simplified labels
		for (Entity e : entities) {
			if (e instanceof DataProperty) {
				LabelExtender.addSimplifiedLabel(e);
			}
			
			if (e instanceof ObjectProperty) {
				LabelExtender.addSimplifiedLabel(e);
			}
		}
		// add labels of symmetric properties
		HashSet<Entity> touched = new HashSet<Entity>();
		for (Entity e1 : entities) {
			touched.add(e1);
			if  (!(e1 instanceof ObjectProperty)) continue;
			ObjectProperty e1Property = (ObjectProperty)e1;
			ObjectProperty e2Property = e1Property.getInverse();
			if (e2Property != null) {
				if (!touched.contains(e2Property)) {
					LabelExtender.addLabelsForInverseProperties(e1Property,e2Property);
				}
			}
		}
		// add labels for compound words
		touched.clear();
		for (Entity e1 : entities) {
			touched.add(e1);
			// if  (!(e1 instanceof Concept)) continue;
			LabelExtender.expandLabelsOfCompound(e1);
		}
		
		
		// add headnouns to concepts based on the superconcepts
		
		touched.clear();
		
		for (Entity e1 : entities) {
			if  (!(e1 instanceof Concept)) continue;
			Concept superConcept = (Concept)e1;
			for (Concept subConcept : superConcept.getSubConcepts()) {
				if (superConcept.isDSubConcept(subConcept)) {
					Label superLabel = superConcept.getPreferedLabel();
					Label subLabel = subConcept.getPreferedLabel();
					if (superLabel.getNumberOfWords() > 2) continue;
					Word headnoun = superLabel.getWord(superLabel.getNumberOfWords() - 1);
					ArrayList<Word> words = new ArrayList<Word>();
					boolean contained = false;
					for (int i = 0; i < subLabel.getNumberOfWords(); i++) {
						Word word = subLabel.getWord(i);
						words.add(subLabel.getWord(i));
						if (word.equals(headnoun)) {
							contained = true;
							break;
						}
					}
					if (contained) continue;
					words.add(headnoun);
					Label extendedLabel = new Label(words);	
					System.out.println("EXTEND " + subLabel + " to " + extendedLabel);
					subConcept.addLabel(extendedLabel);
				}
			}
		}
		
	}

	/**
	* Generates evidence that describes concepts (properties) and their labels  and labels and their words.
	* 
	* @param entities The set of entities for which this evidence is generated.
	* @param ontId The id of the ontology where the entities origin from
	*/
	private void writeEvidenceEntities(HashSet<Entity> entities, String ontId) {		
		ArrayList<String> entityIds = new ArrayList<String>();
		for (Entity e : entities) {
			
			if (e instanceof Concept) {
				int counter = 0;
				entityIds.clear();
				for (Label l : e.getLabels()) {
					int numOfWords = l.getNumberOfWords();
					if (numOfWords <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
						counter++;
						ArrayList<String> paramsE2W = ccc(ontId, e, counter, l);
						EvidenceManager.addGroundAtom("concept" + numOfWords + "Word_o" + ontId , paramsE2W.toArray(new String[paramsE2W.size()]));
						entityIds.add(f(e, counter));
					}
				}
				generateSameAsStatements(ontId, entityIds, "concept");
			}
			
			
			if (e instanceof DataProperty) {
				int counter = 0;
				entityIds.clear();
				for (Label l : e.getLabels()) {
					// System.out.println("LABEL of data property: "  + l);
					int numOfWords = l.getNumberOfWords();
					if (numOfWords <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
						counter++;
						ArrayList<String> paramsE2W = ccc(ontId, e, counter, l);
						EvidenceManager.addGroundAtom("dprop" + numOfWords + "Word_o" + ontId , paramsE2W.toArray(new String[paramsE2W.size()]));
						entityIds.add(f(e, counter));
					}
				}
				generateSameAsStatements(ontId, entityIds, "dprop");
			}
			
			if (e instanceof ObjectProperty) {
				int counter = 0;
				entityIds.clear();
				// System.out.println("Object Property: "  + e.getUri());
				for (Label l : e.getLabels()) {
					// System.out.println("LABEL of object property: "  + l);
					int numOfWords = l.getNumberOfWords();
					if (numOfWords <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
						counter++;
						ArrayList<String> paramsE2W = ccc(ontId, e, counter, l);
						EvidenceManager.addGroundAtom("oprop" + numOfWords + "Word_o" + ontId , paramsE2W.toArray(new String[paramsE2W.size()]));
						entityIds.add(f(e, counter));
					}
				}
				generateSameAsStatements(ontId, entityIds, "oprop");
			}
		
		}
	}

	private void generateSameAsStatements(String ontId, ArrayList<String> entityIds, String type) {
		for (int i = 0; i < entityIds.size(); i++) {
			for (int j = 0; j < entityIds.size(); j++) {
				if (i == j) continue;
				EvidenceManager.addGroundAtom(type + "Same_o" + ontId , new String[]{entityIds.get(i), entityIds.get(j)});
			}	
		}
	}

	private  ArrayList<String> ccc(String ontId, Entity e, int counter, Label l) {
		ArrayList<String> paramsE2W = new ArrayList<String>();
		paramsE2W.add(f(e,counter));
		paramsE2W.addAll(l.getMLWords(ontId));
		return paramsE2W;
	}
	
	/**
	* Creates the concatenation of the URI of e followed by "?" followed by the number i. 
	* 
	* @param e The entity.
	* @param i The suffix number
	* @return The new identifier for (a certain view on) the entity.
	 */
	private String f(Entity e, int i) {
		return e.getUri() + "?" + i;
	}
	
	private void writeEvidenceSemantics(HashSet<Entity> entities, String ontId) {
		for (Entity e1 : entities) {
			if (e1 instanceof Concept) {
				int c = 0;
				for(Label l1 : e1.getLabels()) {
					if (l1.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
					c++;
					Concept e1Concept = (Concept)e1;
					if (e1Concept.isLeafnode()) {
						EvidenceManager.addGroundAtom("leafnode_o" + ontId, f(e1,c));
					}
					if (e1Concept.isRootnode()) {
						EvidenceManager.addGroundAtom("rootnode_o" + ontId, f(e1,c));
					}
				}
			}
			
			
			for (Entity e2 : entities) {
				// concept hierarchy and disjointness
				if (e1 instanceof Concept && e2 instanceof Concept) {
					int c1 = 0;
					for(Label l1 : e1.getLabels()) {
						if (l1.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
						c1++;
						int c2 = 0;
						for(Label l2 : e2.getLabels()) {	
							if (l2.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
							c2++;
							Concept e1Concept = (Concept)e1;
							Concept e2Concept = (Concept)e2;
							if (e1Concept.isSubConcept(e2Concept)) {
								EvidenceManager.addGroundAtom("sub_o" + ontId, f(e1,c1), f(e2,c2));
							}
							if (e1Concept.isDSubConcept(e2Concept)) {
								EvidenceManager.addGroundAtom("dsub_o" + ontId, f(e1,c1), f(e2,c2));
							}
							if (e1Concept.isDisjoint(e2Concept)) {
								EvidenceManager.addGroundAtom("dis_o" + ontId, f(e1,c1), f(e2,c2));
							}
						}
					}
				}
				// domain and range of object properties
				if (e1 instanceof ObjectProperty && e2 instanceof Concept) {
					int c1 = 0;
					for(Label l1 : e1.getLabels()) {
						if (l1.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
						c1++;
						int c2 = 0;
						for(Label l2 : e2.getLabels()) {	
							if (l2.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
							c2++;
							ObjectProperty e1Property = (ObjectProperty)e1;
							Concept e2Concept = (Concept)e2;
							HashSet<Concept> domainConcepts = e1Property.getDomainConcept();
							for (Concept domainConcept : domainConcepts) {
								if (domainConcept.equals(e2Concept)) {
									EvidenceManager.addGroundAtom("opropDom_o" + ontId, f(e1,c1), f(e2,c2));
								}
							}
							HashSet<Concept> rangeConcepts = e1Property.getRangeConcept();
							for (Concept rangeConcept : rangeConcepts) {
								if (rangeConcept.equals(e2Concept)) {
									EvidenceManager.addGroundAtom("opropRan_o" + ontId, f(e1,c1), f(e2,c2));
								}
							}
						}
					}
				}
				// domain of data properties
				if (e1 instanceof DataProperty && e2 instanceof Concept) {
					int c1 = 0;
					for(Label l1 : e1.getLabels()) {
						if (l1.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
						c1++;
						int c2 = 0;
						for(Label l2 : e2.getLabels()) {	
							if (l2.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
							c2++;
							DataProperty e1Property = (DataProperty)e1;
							Concept e2Concept = (Concept)e2;
							HashSet<Concept> domainConcepts = e1Property.getDomainConcept();
							for (Concept domainConcept : domainConcepts) {
								if (domainConcept.equals(e2Concept)) {
									EvidenceManager.addGroundAtom("dpropDom_o" + ontId, f(e1,c1), f(e2,c2));
								}
							}
						}
					}
				}
				if (e1 instanceof ObjectProperty && e2 instanceof ObjectProperty) {
					int c1 = 0;
					for(Label l1 : e1.getLabels()) {
						if (l1.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
						c1++;
						int c2 = 0;
						for(Label l2 : e2.getLabels()) {	
							if (l2.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
							c2++;
							ObjectProperty e1Property = (ObjectProperty)e1;
							ObjectProperty e2Property = (ObjectProperty)e2;
							if (e1Property.getInverse() != null ) {
								if (e1Property.getInverse().equals(e2Property)) {
									EvidenceManager.addGroundAtom("opropInv_o" + ontId, f(e1,c1), f(e2,c2));
								}
							}

						}
					}
				}
				
			}
		}		
	}
	

	
	private void writeEvidenceHypothesis(HashSet<Entity> entities1, HashSet<Entity> entities2, Alignment hypothesis) {
		for (Entity e1 : entities1) {
			if (e1 instanceof Concept) {
				for (Entity e2 : entities2) {
					if (e2 instanceof Concept) {
						Correspondence temp = new Correspondence(e1.getUri(), e2.getUri());
						if (!hypothesis.contained(temp)) {
							int c1 = 0;
							for(Label l1 : e1.getLabels()) {
								if (l1.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
								c1++;
								int c2 = 0;
								for(Label l2 : e2.getLabels()) {	
									if (l2.getNumberOfWords() > Settings.MAX_NUM_OF_WORDS_IN_LABEL) continue;
									c2++;
									EvidenceManager.addGroundAtom("!conceptEQ", f(e1,c1), f(e2,c2));
								}
							}
						}
					}
				}
			}
		}
		
	}

	
	private void writeEvidenceWordSimilarity(String ont1Id, String ont2Id) {
		HashSet<Word> wordsOnt1 = this.ont1.getWords();
		HashSet<Word> wordsOnt2 = this.ont2.getWords();
		boolean show = false;
		for (Word w1 : wordsOnt1) {
			String fullPrefix = "";
			if (w1.getPrefix().equals("C")) fullPrefix = "concept";
			if (w1.getPrefix().equals("D")) fullPrefix = "dprop";
			if (w1.getPrefix().equals("O")) fullPrefix = "oprop";
			double MAX_NUM_OF_SIMILARITIES = Settings.MAX_NUM_OF_SIMILARITIES_CONCEPT;
			if (!w1.getPrefix().equals("C")) MAX_NUM_OF_SIMILARITIES = Settings.MAX_NUM_OF_SIMILARITIES_PROP;
			if (w1.getToken().equals("participant_ksjdfksfsdjhgfjh")) {
				System.out.println("First FOUND !!!!");
				System.out.println("Word type: " + w1.getType());
				show = true;
			}
			else { show = false; }
			
			PriorityQueue<Double> q = new PriorityQueue<Double>();
			for (Word w2 : wordsOnt2) {
				if (!(w1.getPrefix().equals(w2.getPrefix()))) continue;
				double lsim = this.levenstheinWSim.getSimilarity(w1, w2);
				double dsim = this.discoWSim.getSimilarity(w1, w2);
				double dicsim = this.dictionaryWSim.getSimilarity(w1, w2);
				double asim = this. abbreviationWSim.getSimilarity(w1, w2);
				double sim = Math.max(Math.max(lsim, Math.max(dsim,dicsim)), asim);
				// if (show) System.out.println(sim + ": " + w1 + " | " + w2);
				if (sim > 0) {
					q.add(sim);
					if (q.size() > MAX_NUM_OF_SIMILARITIES) {
						q.remove();
					}
				}
			}
			double lowerbound = (!q.isEmpty()) ? q.remove() : 0.00001d;
			if (show) System.out.println("Lower bound for " + w1 +  " = " + lowerbound);
			for (Word w2 : wordsOnt2) {
				if (!(w1.getPrefix().equals(w2.getPrefix()))) continue;
				double lsim = this.levenstheinWSim.getSimilarity(w1, w2);
				double dsim = this.discoWSim.getSimilarity(w1, w2);
				double dicsim = this.dictionaryWSim.getSimilarity(w1, w2);
				double asim = this. abbreviationWSim.getSimilarity(w1, w2);
				double sim = Math.max(Math.max(lsim, Math.max(dsim,dicsim)), asim);
				if (sim < 0.99 ) {
					if (this.ont1.haveDifferentMeaning(w1.getToken(), w2.getToken()) || this.ont2.haveDifferentMeaning(w1.getToken(), w2.getToken())) {
						sim = 0.0;	
					}
					
				}

				if (sim >= lowerbound) {
				
					EvidenceManager.addGroundAtomWeighted(fullPrefix + "WordSim", w1.getMLId(ont1Id), w2.getMLId(ont2Id), "" + sim);
				}
				else {
					EvidenceManager.addGroundAtom("!" + fullPrefix + "WordEQ", w1.getMLId(ont1Id), w2.getMLId(ont2Id));	
				}
			}
		}
	}

	
	
	
	private Alignment runRockitRemote() throws RockitException {
		Alignment alignment = new Alignment();
		
		
		RemoteRockit rockit = new RemoteRockit(Settings.ROCKIT_MODELFILEPATH, Settings.ROCKIT_EVIDENCEFILEPATH);
		RockitResult rr = rockit.run();
		
		ArrayList<String[]> ce = rr.getAtomsOfPredicate("conceptEQ");
		ArrayList<String[]> dpe = rr.getAtomsOfPredicate("dpropEQ");
		ArrayList<String[]> ope = rr.getAtomsOfPredicate("opropEQ");
		
		ArrayList<String[]> equalities = new ArrayList<String[]>();
		equalities.addAll(ce);
		equalities.addAll(dpe);
		equalities.addAll(ope);
		
		System.out.println("Markov Matcher Objective: " + rr.getObjective());
		for (String[] values : equalities) {
			Correspondence c = new Correspondence(values[0], values[1]);
			alignment.add(c);
		}
		
		for (String[] values : ce) {
			out.println("conceptEQ: " + values[0] + ", " +values[1]);
		}
		for (String[] values : dpe) {
			out.println("dpropEQ: " + values[0] + ", " +values[1]);
		}
		for (String[] values : ope) {
			out.println("opropEQ: " + values[0] + ", " +values[1]);
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
	
	/*
	private Alignment runRockitLocal() throws RockitException {
		Alignment alignment = new Alignment();
		Main rockitLocal = new Main();
		String[] args = new String[]{
			"-input",  Settings.ROCKIT_MODELFILEPATH,
			"-data",   Settings.ROCKIT_EVIDENCEFILEPATH,
			"-output", Settings.ROCKIT_LOCALOUT + "xxx"
		};
		
		try {
			rockitLocal.doMain(args);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		} catch (SolveException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ReadOrWriteToFileException e) {
			e.printStackTrace();
		}
		
	
		System.exit(1);
		
		return alignment;
	}
	*/
	
	

	
	
	
	
	
}
