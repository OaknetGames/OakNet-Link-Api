package de.oaknetwork.oaknetlink.api.network;

/**
 * This Exception will be thrown in packet based exceptions
 * 
 * @author Fabian Fila
 */
public class PacketException extends NetException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5713623823224830797L;

	public PacketException(String message) {
		super(message);
	}

}
