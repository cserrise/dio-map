package de.unima.ki.dio.similarity;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unima.ki.dio.entities.Label;
import de.unima.ki.dio.entities.Word;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

public class WNetCSim implements WordSimilarity, CompoundOracle {
	
	private final String wnDir = "nlp/wn3.1.dict/dict";
	private final IRAMDictionary dict = new RAMDictionary(new File(wnDir) , ILoadPolicy.NO_LOAD);
	
	private int degree = 2;
	private boolean includePrevious = true;
	private boolean simOnlyForDegree = true;

	public WNetCSim() {
		try {
			dict.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isKnownCompound(Label l) {
		if(l.getNumberOfWords() >= 2 && dict.getIndexWord(l.toSpacedString(), POS.NOUN) != null){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public double getSimilarity(Word w1, Word w2) {
		return getSimilarity(w1.getToken(), w2.getToken());
	}

	@Override
	public double getSimilarity(String w1, String w2) {
		WordnetStemmer stemmer = new WordnetStemmer(dict);
		List<String> l1 = stemmer.findStems(w1, null);
		List<String> l2 = stemmer.findStems(w2, null);
		if(l1.size() > 0 && l2.size() > 0){
			w1 = l1.get(0);
			w2 = l2.get(0);
			if(dict.getIndexWord(w1, POS.NOUN) == null || dict.getIndexWord(w2, POS.NOUN) == null){
				return 0d;
			}
		}else {
			return 0d;
		}
		double bestSim = 0;
		
		ArrayList<HashSet<ISynsetID>> list1 = new ArrayList<HashSet<ISynsetID>>();
		ArrayList<HashSet<ISynsetID>> list2 = new ArrayList<HashSet<ISynsetID>>();
		
		HashSet<ISynsetID> setForW1 = new HashSet<ISynsetID>();
		HashSet<ISynsetID> setForW2 = new HashSet<ISynsetID>();
		setForW1.addAll(this.getSynsetIDsForWord(w1));
		setForW2.addAll(this.getSynsetIDsForWord(w2));

		list1.add(setForW1);
		list2.add(setForW2);
		
		for(int i = 1; i < this.degree; i++){
			HashSet<ISynsetID> set1 = new HashSet<ISynsetID>();
			HashSet<ISynsetID> set2 = new HashSet<ISynsetID>();
			
			for(ISynsetID id1:setForW1){
				List<IWord> words1 = this.getWordsFromSynset(this.getSynsetForSynsetID(id1));
				for(IWord word1:words1){
					List<ISynsetID> synIDs = this.getSynsetIDsForWord(word1.getLemma());
					for(ISynsetID synID:synIDs){
						set1.add(synID);
					}
				}
			}
			
			for(ISynsetID id2:setForW2){
				List<IWord> words2 = this.getWordsFromSynset(this.getSynsetForSynsetID(id2));
				for(IWord word2:words2){
					List<ISynsetID> synIDs = this.getSynsetIDsForWord(word2.getLemma());
					for(ISynsetID synID:synIDs){
						set2.add(synID);
					}
				}
			}
			if(this.includePrevious){
				for(Set<ISynsetID> prevSet:list1){
					set1.addAll(prevSet);
				}
				for(Set<ISynsetID> prevSet:list2){
					set2.addAll(prevSet);
				}
			}
			list1.add(set1);
			list2.add(set2);
			
			setForW1 = set1;
			setForW2 = set2;
		}
		if(this.simOnlyForDegree){
			bestSim = compare(list1.get(list1.size()-1),list2.get(list2.size()-1));
		}else{
			for(HashSet<ISynsetID>set1:list1){
				for(HashSet<ISynsetID>set2:list2){	
					bestSim = Math.max(bestSim, compare(set1,set2));
				}
			}
		}
		
		
		
		return bestSim;
	}
	
	private double compare(HashSet<ISynsetID> set1, HashSet<ISynsetID> set2){
		final int terms1 = set1.size();
		final int terms2 = set2.size();
		
		final HashSet<ISynsetID> allTokens = new HashSet<ISynsetID>();
		allTokens.addAll(set1);
		allTokens.addAll(set2);
		
		final int commonTerms = (terms1 + terms2) - allTokens.size();
		double sim = (double) (commonTerms) / (double) (Math.pow((double) terms1, 0.5f) * Math.pow((double) terms2, 0.5f));
		return sim;
	}
	
	private List<ISynsetID> getSynsetIDsForWord(String word){
		List<ISynsetID> list = new ArrayList<ISynsetID>();
		IIndexWord idxWord = dict.getIndexWord(word, POS.NOUN);
		if(idxWord == null){
			return null;
		}
		for(IWordID id:idxWord.getWordIDs()){
			list.add(id.getSynsetID());
		}
		return list;
	}
	
	private ISynset getSynsetForSynsetID(ISynsetID id){
		return dict.getSynset(id);
	}
	
	private List<IWord> getWordsFromSynset(ISynset synset){
		List<IWord> list = new ArrayList<IWord>();
		for(IWord word:synset.getWords()){
			list.add(word);
		}
		return list;
	}
	
	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public boolean isIncludePrevious() {
		return includePrevious;
	}

	public void setIncludePrevious(boolean includePrevious) {
		this.includePrevious = includePrevious;
	}

	public boolean isSimOnlyForDegree() {
		return simOnlyForDegree;
	}

	public void setSimOnlyForDegree(boolean simOnlyForDegree) {
		this.simOnlyForDegree = simOnlyForDegree;
	}

	public static void main(String[] args){
		WNetCSim sim = new WNetCSim();
		String[] words = new String[]{"program commitee", "session chair", "subject area", "topic", "last name", "surname", "first name", "chair","reviewed", "participant","evaluated","paper","conference","session","submitted","accepted","fee","member","event","contribution"};
//		String[] words = new String[]{"subject area", "topic"};
		
		DecimalFormat df2 = new DecimalFormat( "0.000" );
		for (int i = 0 ; i < words.length-1; i++) {
			for (int j = i+1 ; j < words.length; j++) {
				String word = words[i];
				String word2 = words[j];
				sim.setDegree(1);
				System.out.print(word + "\t" + word2 + "\t" + df2.format(sim.getSimilarity(word, word2)));
				sim.setDegree(2);
				System.out.print("\t" + df2.format(sim.getSimilarity(word, word2)));
				sim.setDegree(3);
				System.out.print("\t" + df2.format(sim.getSimilarity(word, word2)));
				sim.setDegree(4);
				System.out.print("\t" + df2.format(sim.getSimilarity(word, word2)));
				sim.setDegree(5);
				System.out.print("\t" + df2.format(sim.getSimilarity(word, word2)));
				sim.setDegree(6);
				System.out.print("\t" + df2.format(sim.getSimilarity(word, word2)));
				System.out.println();
			}
			
		}
	}

}
