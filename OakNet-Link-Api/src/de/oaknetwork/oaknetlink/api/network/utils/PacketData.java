package de.oaknetwork.oaknetlink.api.network.utils;

/**
 * This class is used to hold a byte array because Java can't do a call by ref
 * for arrays
 * 
 * @author Fabian Fila
 */
public class PacketData {

	public byte[] data = new byte[0];

	/**
	 * Removes i bytes from the beginning of data
	 */
	public void removeBytes(int i) {
		byte[] newBytes = new byte[data.length - i];
		System.arraycopy(data, i, newBytes, 0, newBytes.length);
		data = newBytes;
	}

	/**
	 * Adds bytesToAppend at end of data
	 */
	public void appendBytes(byte[] bytesToAppend) {
		byte[] newBytes = new byte[data.length + bytesToAppend.length];
		System.arraycopy(data, 0, newBytes, 0, data.length);
		System.arraycopy(bytesToAppend, 0, newBytes, data.length, bytesToAppend.length);
		data = newBytes;
	}

	/**
	 * Adds byteToAppend at end of data
	 */
	public void appendBytes(byte byteToAppend) {
		byte[] newBytes = new byte[data.length + 1];
		System.arraycopy(data, 0, newBytes, 0, data.length);
		newBytes[newBytes.length - 1] = byteToAppend;
		data = newBytes;
	}
}
