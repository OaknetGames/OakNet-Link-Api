package de.oaknetwork.oaknetlink.api.network;

/**
 * This exception is thrown in network based situations
 * 
 * @author Fabian Fila
 */
public class NetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4835637449055463016L;

	public NetException(String message) {
		super(message);
	}
}
