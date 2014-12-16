package de.unima.ki.dio.rockit;

import de.unima.ki.dio.matcher.alignment.Alignment;

public interface RockitWrapper {
	
	public Alignment runRockit(String modelPath, String dataPath, String outPath);

}
