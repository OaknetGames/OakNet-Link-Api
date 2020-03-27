package de.oaknetwork.oaknetlink.api.network.utils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import de.oaknetwork.oaknetlink.api.network.PacketException;

/**
 * This class provides useful methods to decode primitives out of a byte list.
 * The bytes used for the decode will be removed from the list
 * 
 * All values are sent big endian.
 * 
 * @author Fabian Fila
 */
public class PacketInDecoder {

	/**
	 * Decodes an integer out of a byte list. The 4 bytes used for the decode will
	 * be removed
	 * 
	 * @param bytes byte list to decode from
	 * @return the decoded integer
	 * @throws PacketException
	 */
	public static int decodeInt(List<Byte> bytes) throws PacketException {
		if (bytes.size() < 4) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		int result = (bytes.get(0) << 24) | (bytes.get(1) << 16) | (bytes.get(2) << 8) | (bytes.get(3));
		bytes.remove(0);
		bytes.remove(0);
		bytes.remove(0);
		bytes.remove(0);
		return result;
	}

	/**
	 * Decodes a short out of a byte list. The 2 bytes used for the decode will be
	 * removed
	 * 
	 * @param bytes byte list to decode from
	 * @return the decoded short
	 * @throws PacketException
	 */
	public static short decodeShort(List<Byte> bytes) throws PacketException {
		if (bytes.size() < 2) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		short result = (short) ((bytes.get(2) << 8) | (bytes.get(3)));
		bytes.remove(0);
		bytes.remove(0);
		return result;
	}

	/**
	 * Decodes a string out of a byte list. The bytes used for the decode will be
	 * removed
	 * 
	 * @param bytes byte list to decode from
	 * @return the decoded string
	 * @throws PacketException
	 */
	public static String decodeString(List<Byte> bytes) throws PacketException {

		// decode length
		int length = decodeInt(bytes);
		if (bytes.size() < length) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		// decode String
		byte[] stringBytes = new byte[length];
		for (int i = 0; i < length; i++) {
			stringBytes[i] = bytes.get(0);
			bytes.remove(0);
		}
		return new String(stringBytes, StandardCharsets.UTF_8);
	}
}
