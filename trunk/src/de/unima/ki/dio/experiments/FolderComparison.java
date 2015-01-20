package de.unima.ki.dio.experiments;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import de.unima.ki.dio.exceptions.AlignmentException;
import de.unima.ki.dio.exceptions.DioException;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Characteristic;
import de.unima.ki.dio.matcher.alignment.Correspondence;


/**
 *  This class performs experiments to check the results of the simple matcher against the conference dataset.
 */
public class FolderComparison {
	
	// public static String folder1 = "exp/results/markov-x30-minus30/";
	public static String folder1 = "exp/results/temp-2015-1/";
	public static String folder2 = "exp/results/strict-2015-5/";
	public static String refFolder = "exp/conference/references/";
	
	public static Alignment folder1Alignment = new Alignment();
	public static Alignment folder2Alignment = new Alignment();
	public static Alignment referenceAlignment = new Alignment();
	
	
	public static void main(String[] args) throws DioException {
	
		System.out.println("COMPARISON:");
		System.out.println("(1) " +  folder1);
		System.out.println("(2) " +  folder2);
		System.out.println();
		
		String[] ontIds = {
			"cmt",
			"conference",
			"confof",
			"edas",
			"ekaw",
			"iasted",
			"sigkdd"
		};
		

		
		for (int i = 0; i < ontIds.length - 1; i++) {
			for (int j = i+1; j < ontIds.length; j++) {
				String ont1Id = ontIds[i];
				String ont2Id = ontIds[j]; 
				compare(ont1Id, ont2Id);
				
			}			
		}
		
		Characteristic cAll1 = new Characteristic(folder1Alignment, referenceAlignment);
		Characteristic cAll2 = new Characteristic(folder2Alignment, referenceAlignment);
		
		System.out.println("Folder 1:\n" + cAll1);
		System.out.println("Folder 2:\n" + cAll2);
		

		
		
	}

	private static void compare(String ont1Id, String ont2Id) throws DioException, AlignmentException {
		String mappingFile = ont1Id + "-" + ont2Id + ".rdf";

		
		Alignment ali1 = new Alignment(folder1 + mappingFile);
		Alignment ali2 = new Alignment(folder2 + mappingFile);
		Alignment ref = new Alignment(refFolder + mappingFile);
		
		folder1Alignment.add(ali1);
		folder2Alignment.add(ali2);
		referenceAlignment.add(ref);
		
		try {
			PrintWriter pw = new PrintWriter(mappingFile);
			for (Correspondence c : ref) {
				pw.println(c);
			}
			pw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		// System.out.println("NOT FOUND:\n" + ref.minus(ali2));
	
		
		if (!((ali1.minus(ali2).size() == 0)  && (ali2.minus(ali1).size() == 0))) {
			System.out.println("differences found in " + mappingFile);
			
			for (Correspondence c : ali1.minus(ali2)) {
				boolean right = ref.contained(c);
				System.out.println("IN 1 NOT IN 2 " + c + " [" + ((right) ? "T" : "F") +  "]");
				
			}
			for (Correspondence c : ali2.minus(ali1)) {
				boolean right = ref.contained(c);
				System.out.println("IN 2 NOT IN 1 " + c + " [" + ((right) ? "T" : "F") +  "]");
			}
			System.out.println();
			
		
		}
		
		
		

	}

}
