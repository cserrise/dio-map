package de.unima.ki.dio.matcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;


import org.antlr.runtime.RecognitionException;

import com.googlecode.rockit.app.Main;
import com.googlecode.rockit.exception.ParseException;
import com.googlecode.rockit.exception.ReadOrWriteToFileException;
import com.googlecode.rockit.exception.SolveException;


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
	WordSimilarity abbreviationWSim;
	WordSimilarity levenstheinWSim;
	
	private ArrayList<EvidenceManager> groundAtoms = new ArrayList<EvidenceManager>();
	
	
	// public HashMap<String, String>
	
	public MarkovMatcher() {
		this.discoWSim = new DiscoWSim();
		this.abbreviationWSim = new AbbreviationWSim();
		this.levenstheinWSim = new LevenstheinWSim();
	}
	
	public Alignment match(Ontology ont1, Ontology ont2) throws RockitException {
		this.ont1 = ont1;
		this.ont2 = ont2;
		Alignment hypothesis = computeHypotheses(ont1, ont2);
		System.out.println("Hypotheses that have been computed:");
		System.out.println(hypothesis);
		Alignment alignment = computeAlignment(ont1, ont2, hypothesis);
		return alignment;

		
	}
	
	public Alignment computeAlignment(Ontology ont1, Ontology ont2, Alignment hypothesis) throws RockitException {
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
		EvidenceManager.clear();
		writeEvidenceHypothesis(ont1.getEntities(), ont2.getEntities(), hypothesis);	
		writeEvidenceWordSimilarity(ont1.getWords(), "1", ont2.getWords(), "2");
		writeEvidenceEntities(ont1.getEntities(), "1");
		writeEvidenceEntities(ont2.getEntities(), "2");	
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
	
	


	public Alignment computeHypotheses(Ontology ont1, Ontology ont2) throws RockitException {
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
		EvidenceManager.clear();
		writeEvidenceWordSimilarity(ont1.getWords(), "1", ont2.getWords(), "2");
		writeEvidenceEntities(ont1.getEntities(), "1");
		writeEvidenceEntities(ont2.getEntities(), "2");	
		EvidenceManager.write(writer);
		writer.close();
		RockitAdapter rockitAdapter = new RockitAdapter(Settings.ROCKIT_MODELFILEPATH_HYPOTHESIS, Settings.ROCKIT_EVIDENCEFILEPATH + "-HYPO", Settings.ROCKIT_LOCALOUT + "-HYPO");
		Alignment alignment = rockitAdapter.runRockit();
		
		// out.close();
		return alignment;	

	}
	
	

	/**
	* Generates evidence that describes concepts (properties) and their labels  and labels and their words.
	* 
	* @param entities The set of entities for which this evidence is generated.
	* @param ontId The id of the ontology where the entities origin from
	*/
	private void writeEvidenceEntities(HashSet<Entity> entities, String ontId) {		
		for (Entity e : entities) {

			if (e instanceof DataProperty) {
				for (Label l : e.getLabels()) {
					LabelConverter.overwriteBySimplifiedLabel(e, l);
				}
			}
			
			if (e instanceof ObjectProperty) {
				for (Label l : e.getLabels()) {
					LabelConverter.overwriteBySimplifiedLabel(e, l);
				}
			}
			

			if (e instanceof Concept) {
				for (Label l : e.getLabels()) {
					int numOfWords = l.getNumberOfWords();
					if (numOfWords <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
						ArrayList<String> paramsE2W = new ArrayList<String>();
						paramsE2W.add(e.getUri());
						paramsE2W.addAll(l.getMLWords(ontId));
						EvidenceManager.addGroundAtom("concept" + numOfWords + "Word_o" + ontId , paramsE2W.toArray(new String[paramsE2W.size()]));
					}
				}
			}
			
			
			if (e instanceof DataProperty) {
				for (Label l : e.getLabels()) {
					// System.out.println("LABEL of data property: "  + l);
					int numOfWords = l.getNumberOfWords();
					if (numOfWords <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
						ArrayList<String> paramsE2W = new ArrayList<String>();
						paramsE2W.add(e.getUri());
						paramsE2W.addAll(l.getMLWords(ontId));
						EvidenceManager.addGroundAtom("dprop" + numOfWords + "Word_o" + ontId , paramsE2W.toArray(new String[paramsE2W.size()]));
					}
				}
			}
			
			if (e instanceof ObjectProperty) {
				for (Label l : e.getLabels()) {
					// System.out.println("LABEL of object property: "  + l);
					int numOfWords = l.getNumberOfWords();
					if (numOfWords <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
						ArrayList<String> paramsE2W = new ArrayList<String>();
						paramsE2W.add(e.getUri());
						paramsE2W.addAll(l.getMLWords(ontId));
						EvidenceManager.addGroundAtom("oprop" + numOfWords + "Word_o" + ontId , paramsE2W.toArray(new String[paramsE2W.size()]));
					}
				}
			}
		
		}
	}
	
	private void writeEvidenceSemantics(HashSet<Entity> entities, String ontId) {
		for (Entity e1 : entities) {
			if (e1 instanceof Concept) {
				for (Entity e2 : entities) {
					if (e2 instanceof Concept) {
						Concept e1Concept = (Concept)e1;
						Concept e2Concept = (Concept)e2;
						if (e1.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL &&  e2.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
							if (e1Concept.isSubClass(e2Concept)) {
								EvidenceManager.addGroundAtom("sub_o" + ontId, e1.getUri(), e2.getUri());
							}
							if (e1Concept.isDisjoint(e2Concept)) {
								EvidenceManager.addGroundAtom("dis_o" + ontId, e1.getUri(), e2.getUri());
							}
						}
					}
				}
			}
		}
		
		// System.out.println("domain and range of object properties ");
		for (Entity e1 : entities) {
			if (e1 instanceof ObjectProperty) {
				for (Entity e2 : entities) {
					if (e2 instanceof Concept) {
						ObjectProperty e1Property = (ObjectProperty)e1;
						Concept e2Concept = (Concept)e2;
						if (e1.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL &&  e2.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
							HashSet<Concept> domainConcepts = e1Property.getDomainConcept();
							for (Concept domainConcept : domainConcepts) {
								// TODO this is a dirty hack to generate the domain/range for each 
								// if (e2Concept.isSubClass(domainConcept) || domainConcept.equals(e2Concept)) {
								//	this.writelnGroundAtom("opropDom_o" + ontId, e1.getUri(), e2.getUri());
								// }
								if (domainConcept.equals(e2Concept)) {
									EvidenceManager.addGroundAtom("opropDom_o" + ontId, e1.getUri(), e2.getUri());
								}
							}
							HashSet<Concept> rangeConcepts = e1Property.getRangeConcept();
							for (Concept rangeConcept : rangeConcepts) {
								// TODO this is a direty hack to generat the domain/range for each 
								// if (e2Concept.isSubClass(domainConcept) || domainConcept.equals(e2Concept)) {
								//	this.writelnGroundAtom("opropDom_o" + ontId, e1.getUri(), e2.getUri());
								// }
								if (rangeConcept.equals(e2Concept)) {
									EvidenceManager.addGroundAtom("opropRan_o" + ontId, e1.getUri(), e2.getUri());
								}
							}
							
							
						}
					}
				}
			}
		}
		// System.out.println("domain and range of data properties ");
		for (Entity e1 : entities) {
			if (e1 instanceof DataProperty) {
				for (Entity e2 : entities) {
					if (e2 instanceof Concept) {
						DataProperty e1Property = (DataProperty)e1;
						Concept e2Concept = (Concept)e2;
						if (e1.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL &&  e2.getMaxNumOfWords() <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
							HashSet<Concept> domainConcepts = e1Property.getDomainConcept();
							for (Concept domainConcept : domainConcepts) {
								// TODO this is a direty hack to generat the domain/range for each 
								// if (e2Concept.isSubClass(domainConcept) || domainConcept.equals(e2Concept)) {
								//	this.writelnGroundAtom("opropDom_o" + ontId, e1.getUri(), e2.getUri());
								// }
								if (domainConcept.equals(e2Concept)) {
									EvidenceManager.addGroundAtom("dpropDom_o" + ontId, e1.getUri(), e2.getUri());
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
							EvidenceManager.addGroundAtom("!conceptEQ", e1.getUri(), e2.getUri());
						}
					}
				}
			}
		}
		
	}

	
	private void writeEvidenceWordSimilarity(HashSet<Word> wordsOnt1, String ont1Id, HashSet<Word> wordsOnt2, String ont2Id) {
		
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
				double asim = this. abbreviationWSim.getSimilarity(w1, w2);
				double sim = Math.max(Math.max(lsim, dsim), asim);
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
				double asim = this. abbreviationWSim.getSimilarity(w1, w2);
				double sim = Math.max(Math.max(lsim, dsim), asim);
				if (sim >= lowerbound) {
					EvidenceManager.addGroundAtomWeighted(fullPrefix + "WordSim", w1.getMLId(ont1Id), w2.getMLId(ont2Id), "" + normalize(sim));	
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
	
	

	
	
	
	
	
}
