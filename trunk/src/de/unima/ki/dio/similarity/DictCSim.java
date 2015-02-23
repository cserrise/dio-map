package de.unima.ki.dio.similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.unima.ki.dio.entities.Label;
import de.unima.ki.dio.entities.Word;

public class DictCSim implements WordSimilarity, CompoundOracle {

	private int degree = 2;
	
	private HashMap<String, HashSet<String>> enToDe;
	private HashMap<String, HashSet<String>> deToEn;
	
	public DictCSim(){
		enToDe = new HashMap<String, HashSet<String>>();
		deToEn = new HashMap<String, HashSet<String>>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File("nlp/en-de-dic-final.txt")));
			String line ="";
			while((line = reader.readLine()) != null){
				addToDictionary(this.enToDe, line.split("\t")[0].trim(), line.split("\t")[1].trim());
				addToDictionary(this.deToEn, line.split("\t")[1].trim(), line.split("\t")[0].trim());
			}
		}catch(Exception io){
			io.printStackTrace();
		}
	}
	
	
	
	@Override
	public double getSimilarity(Word w1, Word w2) {
		return getSimilarity(w1.getToken(), w2.getToken());
	}

	@Override
	public double getSimilarity(String w1, String w2) {
		HashSet<String> vector1 = new HashSet<String>();
		HashSet<String> vector2 = new HashSet<String>();
		
		vector1.add(w1.toLowerCase());
		vector2.add(w2.toLowerCase());
		
		for(int i = 0; i < this.degree; i++){
			if(vector1.size() == 0 || vector2.size() == 0) break;
			
			if(i % 2 == 0){
				vector1 = translate("en", vector1);
				vector2 = translate("en", vector2);
			}else{
				vector1 = translate("de", vector1);
				vector2 = translate("de", vector2);
			}
		}
		if(vector1.size() == 0 || vector2.size() == 0) return 0d;
		
		System.out.println("Word 1 = " + w1);
		System.out.println(vector1);
		System.out.println("Word 2 = " + w2);
		System.out.println(vector2);
		
		final int termsInString1 = vector1.size();
		final int termsInString2 = vector2.size();
		
		final Set<String> allTokens = new HashSet<String>();
		allTokens.addAll(vector1);
		allTokens.addAll(vector2);
		
		final int commonTerms = (termsInString1 + termsInString2) - allTokens.size();
		float sim = (float) (commonTerms) / (float) (Math.pow((float) termsInString1, 0.5f) * Math.pow((float) termsInString2, 0.5f));
		if (sim > 0 && !w1.equals(w2)) {
			// System.out.println("DicSim " + w1 + " = " + w2 + " => " + sim);
		}
		
		return Math.pow(sim, 0.4);
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	@Override
	public boolean isKnownCompound(Label l) {
		if(l.getNumberOfWords() >= 2 && enToDe.containsKey(l.toSpacedString().toLowerCase())){
			return true;
		}else{
			return false;
		}
	}
	
	private void addToDictionary(HashMap<String,HashSet<String>> dic, String key, String value){
		if(dic.containsKey(key)){
			HashSet<String> temp = dic.get(key);
			temp.add(value);
			dic.put(key, temp);
		}else{
			HashSet<String> temp = new HashSet<String>();
			temp.add(value);
			dic.put(key, temp);
		}
	}
	
	private HashSet<String> translate(String currLang, HashSet<String> words){
		HashSet<String> returnVector = new HashSet<String>();
		
		if(currLang.equals("de")){
			for(String word:words){
				HashSet<String> temp = this.deToEn.get(word);
				if(temp != null){
					for(String translation:temp){
						returnVector.add(translation);
					}
				}
			}
		}else if(currLang.equals("en")){
			for(String word:words){
				HashSet<String> temp = this.enToDe.get(word);
				if(temp != null){
					for(String translation:temp){
						returnVector.add(translation);
					}
				}
			}
		}	
		return returnVector;
	}
	
	public static void main(String[] args) throws Exception{
		DictCSim sim = new DictCSim();
		
		// String[] words = new String[]{"program commitee", "session chair", "subject area", "topic", "last name", "surname", "first name", "chair","reviewed", "participant","evaluated","paper","conference","session","submitted","accepted","fee","member","event","contribution"};
		String[] words = new String[]{"abstract", "departure", "place", "location"};
		
		sim.setDegree(2);
		DecimalFormat df2 = new DecimalFormat( "0.000" );
		for (int i = 0 ; i < words.length-1; i++) {
			for (int j = i+1 ; j < words.length; j++) {
				String word = words[i];
				String word2 = words[j];
				sim.setDegree(1);
				System.out.print(word + "\t" + word2 + "\t" + df2.format(sim.getSimilarity(word, word2)));
				sim.setDegree(2);
				System.out.print("\t" + df2.format(sim.getSimilarity(word, word2)));

			}
		}
		
	}	
}
