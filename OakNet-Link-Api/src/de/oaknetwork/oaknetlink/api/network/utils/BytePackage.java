package de.oaknetwork.oaknetlink.api.network.utils;

/**
 * This is used to encapsule byte packages for the transmission of mcPackets
 * 
 * @author Fabian Fila
 */
public class BytePackage {

	/**
	 * The data contained in the bytePackage
	 */
	public byte[] data;
	
	/**
	 * Creates a new BytePackage with the given data
	 * 
	 * @param data
	 */
	public BytePackage(byte[] data) {
		this.data=data;
	}
}
