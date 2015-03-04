package de.unima.ki.dio.similarity;

import de.unima.ki.dio.entities.Label;
import de.unima.ki.dio.entities.Word;

public class CombinedCSim implements WordSimilarity, CompoundOracle {
	
	private WNetCSim wordnet;
	private DiscoWSim disco;
	private DictCSim dictionary;
	private WordSimilarity abbreviationWSim;
	private WordSimilarity levenstheinWSim;
	
	
	public CombinedCSim(){
		this.wordnet = new WNetCSim();
		this.disco = new DiscoWSim();
		this.dictionary = new DictCSim();
		this.wordnet.setDegree(1);
		this.dictionary.setDegree(1);
		this.abbreviationWSim = new AbbreviationWSim();
		this.levenstheinWSim = new LevenstheinWSim();
	}
	
	
	public double getSimilarity(Word w1, Word w2) {
		return getSimilarity(w1.getToken(), w2.getToken());
	}


	public double getSimilarity(String w1, String w2) {
		
		double wor = wordnet.getSimilarity(w1, w2);
		double dis = disco.getSimilarity(w1, w2);
		double dic = dictionary.getSimilarity(w1, w2);
		// System.out.println(w1 + "=" + w2 + ": " + wor + "WOR " + dis + "DIS " + dic + "DIC");
		double avg = ((wor + dis + dic) / 3d) * 2.0d;
		if (avg > 0.9d) {
			return 0.9;
		}
		
		double abb = this.abbreviationWSim.getSimilarity(w1, w2);
		double lev = this.levenstheinWSim.getSimilarity(w1, w2);
		return Math.max(avg, Math.max(abb, lev));
		
	}

	public boolean isKnownCompound(Label l) {
		boolean wordnetKnows = ((CompoundOracle)this.wordnet).isKnownCompound(l);
		boolean dictionaryKnows = ((CompoundOracle)this.dictionary).isKnownCompound(l);
		return wordnetKnows || dictionaryKnows;
	}	
}
