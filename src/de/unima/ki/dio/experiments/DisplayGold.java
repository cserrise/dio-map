package de.unima.ki.dio.experiments;
import de.unima.ki.dio.exceptions.AlignmentException;
import de.unima.ki.dio.matcher.alignment.*;



public class DisplayGold {

public static void main(String[] args) throws AlignmentException {
		
		Alignment m = new Alignment("E:/coding/code/alcomo2012-v1.01/testdata/conference/references/iasted-sigkdd.rdf");
		int counter = 1;
		for (Correspondence c : m) {
			System.out.println(counter++ + " " + c);
		}
		
		

	}

}