package de.unima.ki.dio.rockit;

import de.unima.ki.dio.Settings;
import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.rockit.local.LocalRockitWrapper;
import de.unima.ki.dio.rockit.remote.RemoteRockitWrapper;

public class RockitAdapter {
	
	private String modelPath;
	private String dataPath;
	private String outPath;
	
	private RockitWrapper rockit = null;
	
	public RockitAdapter(String modelPath, String dataPath, String outPath) {
		this.modelPath = modelPath;
		this.dataPath = dataPath;
		this.outPath = outPath;
		if (Settings.USE_LOCAL_ROCKIT) {
			this.rockit = new LocalRockitWrapper(); 
		}
		else {
			this.rockit = new RemoteRockitWrapper(); 
		}
	}
	
	public Alignment runRockit() {
		return this.rockit.runRockit(modelPath, dataPath, outPath);
		

	}
	
	
}
