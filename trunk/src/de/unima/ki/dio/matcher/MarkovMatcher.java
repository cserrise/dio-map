package de.unima.ki.dio.matcher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

// import org.obolibrary.oboformat.model.Clause;


// import de.dwslab.ai.riskmanagement.abduction.existential.ExistentialAPI;
import de.unima.ki.dio.Settings;
import de.unima.ki.dio.entities.*;
import de.unima.ki.dio.exceptions.RockitException;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Correspondence;
import de.unima.ki.dio.matcher.evidence.Clause;
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
	WordSimilarity wordNetSim;
	
	
	
	Ontology ont1;
	Ontology ont2;
	
	// public HashMap<String, String>
	
	public MarkovMatcher() {
		this.discoWSim = new DiscoWSim();
		this.abbreviationWSim = new AbbreviationWSim();
		this.levenstheinWSim = new LevenstheinWSim();
		WNetCSim wordNetSim = new WNetCSim();
		wordNetSim.setDegree(1);
		this.wordNetSim = wordNetSim;
		
		// this.dictionaryWSim = new DictionaryMock();
		// this.dictionaryWSim = new DictCSim();
		// ((DictCSim)this.dictionaryWSim).setDegree(2);
	}
	
	public Alignment match(Ontology ont1, Ontology ont2) throws RockitException {
		this.ont1 = ont1;
		this.ont2 = ont2;
		Alignment hypothesis = computeHypotheses();
		System.out.println("Hypotheses that have been computed:");
		System.out.println();
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
		
		try {
			createExtendedModelFile(Settings.ROCKIT_MODELFILEPATH, Settings.ROCKIT_MODELFILEPATH_X);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if (Settings.ENSURE_COHERENCY) {
			writeEvidenceSemantics(ont1.getEntities(), "1");
			writeEvidenceSemantics(ont2.getEntities(), "2");
		}
		EvidenceManager.write(writer);
		writer.close();
	
		
		RockitAdapter rockitAdapter = new RockitAdapter(Settings.ROCKIT_MODELFILEPATH_X, Settings.ROCKIT_EVIDENCEFILEPATH, Settings.ROCKIT_LOCALOUT);
		Alignment alignment = rockitAdapter.runRockit();
		
		out.close();
		return alignment;
	}
	
	private void createExtendedModelFile(String modelFile, String extendedModelfile) throws IOException {
		PrintWriter pw = new PrintWriter(extendedModelfile);
		BufferedReader br = new BufferedReader(new FileReader(modelFile));
 
		String line;
		while ((line = br.readLine()) != null) {
			pw.println(line);
		}
		br.close();
 
		pw.println("");
		pw.println("// *********************************");
		pw.println("// **** automatically generated ****");
		pw.println("// *********************************");
		for (Clause c : EvidenceManager.getClauses()) {
			pw.println(c);
		}
		
		pw.flush();
		pw.close();
		
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
			if  (!(e1 instanceof Concept)) continue;
			LabelExtender.expandLabelsOfCompound(e1);
		}
		
		
		// add headnouns to concepts based on the superconcepts
		/*
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
		*/
		
	}

	/**
	* Generates evidence that describes concepts (properties) and their labels  and labels and their words.
	* 
	* @param entities The set of entities for which this evidence is generated.
	* @param ontId The id of the ontology where the entities origin from
	*/
	private void writeEvidenceEntities(HashSet<Entity> entities, String ontId) {		
		for (Entity e : entities) {
			String typePrefix = null;
			if (e instanceof Concept) {
				typePrefix = "concept";
			}
			if (e instanceof DataProperty) {
				typePrefix = "dprop";
			}	
			if (e instanceof ObjectProperty) {
				typePrefix = "oprop";
			}
			int counter = 0;
			for (Label l : e.getLabels()) {
				int numOfWords = l.getNumberOfWords();
				if (numOfWords <= Settings.MAX_NUM_OF_WORDS_IN_LABEL) {
					counter++;
					ArrayList<String> paramsL2W = getLabelWordsParams(ontId, e, counter, l);
					ArrayList<String> paramsE2L = getEntityLabelParams(ontId, e, counter, l);
					EvidenceManager.addGroundAtom(typePrefix + "L" + numOfWords + "Word_o" + ontId , paramsL2W.toArray(new String[paramsL2W.size()]));
					EvidenceManager.addGroundAtom(typePrefix + "Label_o" + ontId , paramsE2L.toArray(new String[paramsE2L.size()]));
				}
			}
		}
	}

	/**
	* Creates and returns an array list that contains the label of an entity followed by the words of this label.
	* 
	* @param ontId The id of the ontology to which this entity belongs to.
	* @param e the entity.
	* @param counter The id of the label.
	* @param One of the labels of the entity.
	* @return
	 */
	private  ArrayList<String> getLabelWordsParams(String ontId, Entity e, int counter, Label l) {
		ArrayList<String> paramsL2W = new ArrayList<String>();
		paramsL2W.add(getLabelUri(e,counter));
		paramsL2W.addAll(l.getMLWords(ontId));
		return paramsL2W;
	}
	
	/**
	* Creates and returns an array with two elements. The first is the uri of an entity and the second
	* the uri of a label of that entity. 
	* 
	* @param ontId
	* @param e
	* @param counter
	* @param l
	* @return
	*/
	private  ArrayList<String> getEntityLabelParams(String ontId, Entity e, int counter, Label l) {
		ArrayList<String> paramsE2L = new ArrayList<String>();
		paramsE2L.add(e.getUri());
		paramsE2L.add(getLabelUri(e,counter));
		return paramsE2L;
	}
	
	
	
	/**
	* Creates the concatenation of the URI of e followed by "?" followed by the number i, which is the labels of an entity.
	* 
	* @param e The entity.
	* @param i The suffix number
	* @return The new identifier for (a certain view on) the entity.
	 */
	private String getLabelUri(Entity e, int i) {
		return e.getUri() + "?" + i;
	}
	
	private void writeEvidenceSemantics(HashSet<Entity> entities, String ontId) {
		for (Entity e1 : entities) {
			if (e1 instanceof Concept) {
				Concept e1Concept = (Concept)e1;
				if (e1Concept.isLeafnode()) {
					EvidenceManager.addGroundAtom("leafnode_o" + ontId, e1.getUri());
				}
				if (e1Concept.isRootnode()) {
					EvidenceManager.addGroundAtom("rootnode_o" + ontId, e1.getUri());
				}
			}
			
			
			for (Entity e2 : entities) {
				// concept hierarchy and disjointness
				if (e1 instanceof Concept && e2 instanceof Concept) {

					Concept e1Concept = (Concept)e1;
					Concept e2Concept = (Concept)e2;
					if (e1Concept.isSubConcept(e2Concept)) {
						EvidenceManager.addGroundAtom("sub_o" + ontId, e1.getUri(), e2.getUri());
					}
					if (e1Concept.isDSubConcept(e2Concept)) {
						EvidenceManager.addGroundAtom("dsub_o" + ontId, e1.getUri(), e2.getUri());
					}
					if (e1Concept.isDisjoint(e2Concept)) {
						EvidenceManager.addGroundAtom("dis_o" + ontId,  e1.getUri(), e2.getUri());
					}
					
				}
				// domain and range of object properties
				if (e1 instanceof ObjectProperty && e2 instanceof Concept) {
					ObjectProperty e1Property = (ObjectProperty)e1;
					Concept e2Concept = (Concept)e2;
					HashSet<Concept> domainConcepts = e1Property.getDomainConcept();
					for (Concept domainConcept : domainConcepts) {
						if (domainConcept.equals(e2Concept)) {
							EvidenceManager.addGroundAtom("opropDom_o" + ontId,  e1.getUri(), e2.getUri());
						}
					}
					HashSet<Concept> rangeConcepts = e1Property.getRangeConcept();
					for (Concept rangeConcept : rangeConcepts) {
						if (rangeConcept.equals(e2Concept)) {
							EvidenceManager.addGroundAtom("opropRan_o" + ontId, e1.getUri(), e2.getUri());
						}
					}
				}
				// domain of data properties
				if (e1 instanceof DataProperty && e2 instanceof Concept) {	
					DataProperty e1Property = (DataProperty)e1;
					Concept e2Concept = (Concept)e2;
					HashSet<Concept> domainConcepts = e1Property.getDomainConcept();
					for (Concept domainConcept : domainConcepts) {
						if (domainConcept.equals(e2Concept)) {
							EvidenceManager.addGroundAtom("dpropDom_o" + ontId,  e1.getUri(), e2.getUri());
						}
					}
				}
				if (e1 instanceof ObjectProperty && e2 instanceof ObjectProperty) {
					ObjectProperty e1Property = (ObjectProperty)e1;
					ObjectProperty e2Property = (ObjectProperty)e2;
					if (e1Property.getInverse() != null ) {
						if (e1Property.getInverse().equals(e2Property)) {
							EvidenceManager.addGroundAtom("opropInv_o" + ontId,  e1.getUri(), e2.getUri());
						}
					}
				}
			}
		}		
	}

	
	private void writeEvidenceHypothesis(HashSet<Entity> entities1, HashSet<Entity> entities2, Alignment hypothesis) {
		int counter = 0;
		for (Entity e1 : entities1) {
			for (Entity e2 : entities2) {
				String type = null;
				if (e1 instanceof Concept && e2 instanceof Concept) { type = "concept"; }
				if (e1 instanceof DataProperty && e2 instanceof DataProperty) { type = "dprop"; }
				if (e1 instanceof ObjectProperty && e2 instanceof ObjectProperty) {
					type = "oprop";
				}
				if (type == null) {
					continue;
				}

				Correspondence temp = new Correspondence(e1.getUri(), e2.getUri());
				if (!hypothesis.contained(temp)) {
					EvidenceManager.addGroundAtom("!" + type + "EQ", e1.getUri(), e2.getUri());
				}
				else {
					// !conceptEQ(x, y) v conceptLabelEQ(l1X, l1Y) v conceptLabelEQ(l1X, l2Y) v conceptLabelEQ(l2X, l1Y) v conceptLabelEQ(l1X, l2Y).
					// transformed to the following three lines
					// !conceptEQ(x, y) v conceptEQxxx(x, l1Y) v conceptEQxxx(x, l1Y).
					// !conceptEQxxx(x, l1Y) v conceptLabelEQ(l1X, l1Y) v conceptLabelEQ(l2X, l1Y).
					// !conceptEQxxx(x, l2Y) v conceptLabelEQ(l1X, l2Y) v conceptLabelEQ(l2X, l2Y).
					
					Clause clause = new Clause();
					clause.addLiteral("!" + type + "EQ", e1.getUri(), e2.getUri());
					// if (e1.getLabels().size() * e2.getLabels().size() > 8) {
					// 	continue;
					// }
					int c1 = 0;
					
					for (Label l1 : e1.getLabels()) {
						c1++;
						clause.addLiteral(type + "EQxxx", getLabelUri(e1, c1), e2.getUri());
						Clause xclause = new Clause();
						xclause.addLiteral("!" + type + "EQxxx", getLabelUri(e1, c1), e2.getUri());
						int c2 = 0;
						for (Label l2 : e2.getLabels()) {
							c2++;
							counter++;
							xclause.addLiteral(type + "LabelEQ", getLabelUri(e1, c1), getLabelUri(e2, c2));
							// System.out.println("... " + counter);
						}
						EvidenceManager.addClause(xclause);
						
					}
					EvidenceManager.addClause(clause);		
					EvidenceManager.addClause(new Clause()); // empty clause, just for formating the output	
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
			if (w1.getToken().equals("pprprp")) {
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
				double wsim = this.wordNetSim.getSimilarity(w1, w2);
				double asim = this. abbreviationWSim.getSimilarity(w1, w2);
				double sim = getMax(lsim, dsim, asim, wsim);
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
				double wsim = this.wordNetSim.getSimilarity(w1, w2);
				double asim = this. abbreviationWSim.getSimilarity(w1, w2);
				double sim = getMax(lsim, dsim, asim, wsim);

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
	
	private double getMax(double ... values ) {
		if (values.length > 1) {
			double[] vals = new double[values.length-1];
			vals[0] = Math.max(values[0], values[1]);
			for (int i = 1; i < vals.length; i++) {
				vals[i] = values[i+1];	
			}
			return getMax(vals);
		}
		else {
			return values[0];
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
