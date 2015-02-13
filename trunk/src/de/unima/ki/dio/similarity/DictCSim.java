package de.unima.ki.dio.similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;

import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import de.unima.ki.dio.entities.Word;

public class DictCSim implements WordSimilarity {

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
		CosineSimilarity cosSim = new CosineSimilarity();
		HashSet<String> vector1 = new HashSet<String>();
		HashSet<String> vector2 = new HashSet<String>();
		
		vector1.add(w1.toLowerCase());
		vector2.add(w2.toLowerCase());
		
		for(int i = 0; i < this.degree; i++){
			if(vector1.size() == 0 || vector2.size() == 0) break;
			
			vector1 = translate("de",translate("en", vector1));
			vector2 = translate("de",translate("en", vector2));
		}
		
		String compare1="",compare2="";
		
		for(String temp:vector1){
			compare1 = compare1 + " " + temp;
		}
		compare1 = compare1.trim();
		for(String temp:vector2){
			compare2 = compare2 + " " + temp;
		}
		compare2 = compare2.trim();
		
		return cosSim.getSimilarity(compare1, compare2);
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
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
		String[] words = new String[]{"chair","participant","evaluated","paper","conference","session","submitted","accepted","fee","member","event","topic","contribution"};
		sim.setDegree(1);
		DecimalFormat df2 = new DecimalFormat( "0.000" );
		for(String word:words){
			for(String word2:words){
				sim.setDegree(1);
				System.out.print(word + "\t" + word2 + "\t" + df2.format(sim.getSimilarity(word, word2)));
				sim.setDegree(2);
				System.out.print("\t" + df2.format(sim.getSimilarity(word, word2)));
				sim.setDegree(3);
				System.out.println("\t" + df2.format(sim.getSimilarity(word, word2)));
			}
		}
		
		
//		System.out.println(sim.getSimilarity("subject area", "topic"));

	}
}
