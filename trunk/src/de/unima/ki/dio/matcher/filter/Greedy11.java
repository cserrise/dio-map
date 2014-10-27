package de.unima.ki.dio.matcher.filter;

import de.unima.ki.dio.matcher.alignment.Alignment;
import de.unima.ki.dio.matcher.alignment.Correspondence;

/**
 * Performs a straight forward greey extraction to select a 1:1 alignment from a given alignment.
 * 
 */
public class Greedy11 {
	
	/**
	* Applies a 1:1 greedy filter.
	* 
	* @param alignment The alignemnt that will be filtered,
	* @return The filtered alignment.
	*/
	public static Alignment filter(Alignment alignment) {
		Alignment filtered = new Alignment();
		alignment.sortDescending();
		for (int down = 0; down < alignment.size(); down++) {
			Correspondence current = alignment.get(down);
			boolean fine = true;
			for (Correspondence fc : filtered) {
				if (fc.getUri1().equals(current.getUri1()) || fc.getUri2().equals(current.getUri2())) {
					fine = false;
				}
			}
			if (fine) {
				filtered.add(current);
			}
		}
		return filtered;
	}
	
}
