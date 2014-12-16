package de.unima.ki.dio.rockit.remote;

import java.util.ArrayList;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.exceptions.RockitException;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Correspondence;
import de.unima.ki.dio.rockit.RockitWrapper;

public class RemoteRockitWrapper implements RockitWrapper {

	public Alignment runRockit(String modelPath, String dataPath, String outPath) {

	Alignment alignment = new Alignment();
		
		
		RemoteRockit rockit = new RemoteRockit(Settings.ROCKIT_MODELFILEPATH, Settings.ROCKIT_EVIDENCEFILEPATH);
		RockitResult rr = null;
		try {
			rr = rockit.run();
		} catch (RockitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String[]> ce = rr.getAtomsOfPredicate("conceptEQ");
		
		System.out.println("Markov Matcher Objective: " + rr.getObjective());
		for (String[] values : ce) {
			Correspondence c = new Correspondence(values[0], values[1]);
			alignment.add(c);
		}
		
		for (String[] values : ce) {
			//out.println("conceptEQ: " + values[0] + ", " +values[1]);
		}
		ArrayList<String[]> le = rr.getAtomsOfPredicate("labelEQ");
		for (String[] values : le) {
			//out.println("labelEQ: " + values[0] + ", " +values[1]);
		}
		ArrayList<String[]> we = rr.getAtomsOfPredicate("wordEQ");
		for (String[] values : we) {
			//out.println("wordEQ: " + values[0] + ", " +values[1]);
		}

		
		ArrayList<String[]> b1 = rr.getAtomsOfPredicate("blindWord_o1");
		ArrayList<String[]> b2 = rr.getAtomsOfPredicate("blindWord_o2");
		for (String[] values : b1) {
			//out.println("blindWord_o1: " + values[0]);
		}
		for (String[] values : b2) {
			//out.println("blindWord_o2: " + values[0]);
		}
		
		return alignment;
		
	}

}
