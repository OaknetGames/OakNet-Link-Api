package de.oaknetwork.oaknetlink.api.network;

/**
 * This Exception will be thrown in packet based exceptions
 * 
 * @author Fabian Fila
 */
public class PacketException extends NetException{
	
	public PacketException(String message) {
		super(message);
	}

}
