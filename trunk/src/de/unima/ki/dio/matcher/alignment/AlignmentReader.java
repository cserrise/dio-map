package de.unima.ki.dio.matcher.alignment;

import de.unima.ki.dio.exceptions.AlignmentException;

/**
* The MappingReader defines the interface for its implementing classes that can be used to read
* Mappings from xml as well as txt-files. 
*
*/

public interface AlignmentReader {
	
	public Alignment getAlignment(String filepath) throws AlignmentException;
		
}