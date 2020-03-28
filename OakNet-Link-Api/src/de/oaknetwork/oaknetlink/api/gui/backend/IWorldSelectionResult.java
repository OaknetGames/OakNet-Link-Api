package de.oaknetwork.oaknetlink.api.gui.backend;

/**
 * Like Runnable but with a worldName rgument
 * 
 * @author Fabian Fila
 */
public interface IWorldSelectionResult {
	
	/**
	 * worldname will be "" when nothing is selected
	 * 
	 * @param worldName
	 */
	public void run(String worldName);
}
