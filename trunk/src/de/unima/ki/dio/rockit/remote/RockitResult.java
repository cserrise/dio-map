package de.unima.ki.dio.rockit.remote;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RockitResult {
	
	private double objective;
	private ArrayList<String[]> lines = new ArrayList<String[]>();
	
	
	
	public void setObjective(double objective) {
		this.objective = objective;	
	}
	
	public double getObjective() {
		return this.objective;
		
	}
	
	public void addLine(String line) {
		String[] splittedLine = this.splitLine(line);
		if (splittedLine != null) {
			this.lines.add(splittedLine);
		}
	}
	
	private String[] splitLine(String line) {
		line = line.trim();

		String[] predRest = line.split("\\(\"");
		String[] params = predRest[1].split("\",\\s?\"");
		String[] lineTokens = new String[params.length+1];
		lineTokens[0] = predRest[0];
		for (int i = 0; i < params.length-1; i++) {
			lineTokens[i+1] = params[i];
		}
		lineTokens[params.length]  = params[params.length-1].replace("\")", "");
		return lineTokens;
		
	}
	
	public ArrayList<String[]> getAtomsOfPredicate(String predicate) {
		ArrayList<String[]> atomsOfPredicate = new ArrayList<String[]>();
		for (String[] tokens : this.lines) {
			if (tokens[0].equals(predicate)) {
				String[] atom = new String[tokens.length-1];
				atom = Arrays.copyOfRange(tokens, 1, tokens.length);
				atomsOfPredicate.add(atom);
			}
		}
		return atomsOfPredicate;
		
	}
	
	public static void main(String[] args) {
		RockitResult rr = new RockitResult();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("tmp/out.db"));
	        String line = br.readLine();
	        while (null != (line = br.readLine())) {
	            // line = br.readLine();
	           System.out.println("LINE:" + line);
	            rr.addLine(line);
	        }
	        ArrayList<String[]> values = rr.getAtomsOfPredicate("conceptEQ");
	        for (String[] value : values) {
	        	System.out.println(value[0] + " ::: "+ value[1]);
	        	
	        	
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        
    
		
		
		
	}

}
