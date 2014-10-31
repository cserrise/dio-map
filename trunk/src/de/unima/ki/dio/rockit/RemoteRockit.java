package de.unima.ki.dio.rockit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.exceptions.RockitException;

public class RemoteRockit {
	
	
	private File model;
	private File evidence;
	
	public RemoteRockit(String modelFilepath, String evidenceFilepath) {
		this.model = new File(modelFilepath);
		this.evidence = new File(evidenceFilepath);		
	}
	
	public RockitResult run() throws RockitException {
		try {
			PyRockitAPI.runMapRockit(model, evidence, "" + Settings.ROCKIT_GAP);
			double objective = PyRockitAPI.getObjective();
			List<String> mapstate = PyRockitAPI.getMAPState();			
			RockitResult rr = new RockitResult();
			rr.setObjective(objective);	
			for (String line : mapstate) {
				rr.addLine(line);
				// System.out.println(line);

			}
			return rr;
		}
		catch (Exception e) {
			throw new RockitException(RockitException.UNKNOWN_ERROR, "failure when calling rockit", e);
		}


	}


	

	public static void main(String[] args) throws Exception {
		File input = new File("mod/model1.ml");
		File data = new File("tmp/evidence.db");
		
	}

}
