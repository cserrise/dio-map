package de.unima.ki.dio.rockit.local;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.antlr.runtime.RecognitionException;

import com.googlecode.rockit.app.Main;
import com.googlecode.rockit.exception.ParseException;
import com.googlecode.rockit.exception.ReadOrWriteToFileException;
import com.googlecode.rockit.exception.SolveException;

import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Correspondence;
import de.unima.ki.dio.rockit.RockitResult;
import de.unima.ki.dio.rockit.RockitWrapper;

public class LocalRockitWrapper implements RockitWrapper{

	/*
	public static void main(String[] args) {
		LocalRockitWrapper r = new LocalRockitWrapper();
		r.runRockit("temp/test/prog.mln", "temp/test/evidence.db", "temp/test/out.db");
	}
	*/

	public Alignment runRockit(String modelPath, String dataPath, String outPath) {
		Alignment alignment = new Alignment();
		Main rockitLocal = new Main();
		String[] args = new String[]{
			"-input",  modelPath,
			"-data",   dataPath,
			"-output", outPath
		};
		
		try {
			rockitLocal.doMain(args);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		catch (RecognitionException e) {
			e.printStackTrace();
		}
		catch (SolveException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (ReadOrWriteToFileException e) {
			e.printStackTrace();
		}
		

	    BufferedReader br;
	    RockitResult rr = new RockitResult();
		try {
			br = new BufferedReader(new FileReader(outPath));
	        String line;
	        while (null != (line = br.readLine())) {
	            // line = br.readLine();
	           //  System.out.println("LINE:" + line);
	            rr.addLine(line);
	        }
	        br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ArrayList<String[]> ce = rr.getAtomsOfPredicate("conceptEQ");
		ArrayList<String[]> dpe = rr.getAtomsOfPredicate("dpropEQ");
		ArrayList<String[]> ope = rr.getAtomsOfPredicate("opropEQ");
		ArrayList<String[]> equalities = new ArrayList<String[]>();
		equalities.addAll(ce);
		equalities.addAll(dpe);
		equalities.addAll(ope);
		
		Set<Correspondence> correspondences = getCorrespondences(equalities);
		for (Correspondence c : correspondences) {
			alignment.add(c);
		}
		return alignment;
	}
	
	private HashSet<Correspondence> getCorrespondences(ArrayList<String[]> eqs) {
		HashSet<Correspondence> correspondences = new HashSet<Correspondence>();
		for (String[] entities : eqs) {
			String e1 = removeSuffix(entities[0]);
			String e2 = removeSuffix(entities[1]);
			Correspondence c = new Correspondence(e1, e2);
			correspondences.add(c);
		}
		return correspondences;
	}

	private String removeSuffix(String s) {
		int lastIndex = s.lastIndexOf("?");
		return s.substring(0, lastIndex);
	}
	
	
	
	


}
