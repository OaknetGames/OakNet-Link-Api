package de.oaknetwork.oaknetlink.api.network.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class provides useful methods to encodes primitives in a byte list.
 * 
 * All values are sent big endian.
 * 
 * @author Fabian Fila
 */
public class PacketOutEncoder {

	/**
	 * Encodes an integer in a byte list
	 * 
	 * @param bytes       the list in which the encoded bytes will be put
	 * @param intToEncode the integer which should be encoded
	 */
	public static void encodeInt(List<Byte> bytes, int intToEncode) {
		bytes.add((byte) ((intToEncode & 0xFF000000) >> 24));
		bytes.add((byte) ((intToEncode & 0x00FF0000) >> 16));
		bytes.add((byte) ((intToEncode & 0x0000FF00) >> 8));
		bytes.add((byte) (intToEncode & 0x000000FF));
	}

	/**
	 * Encodes a short in a byte list
	 * 
	 * @param bytes         the list in which the encoded bytes will be put
	 * @param shortToEncode the short which should be encoded
	 */
	public static void encodeShort(List<Byte> bytes, short shortToEncode) {
		bytes.add((byte) ((shortToEncode & 0xFF00) >> 8));
		bytes.add((byte) (shortToEncode & 0x00FF));
	}

	/**
	 * Encodes a string in a byte list
	 * 
	 * @param bytes          the list in which the encoded bytes will be put
	 * @param stringToEncode the string which should be encoded
	 */
	public static void encodeString(List<Byte> bytes, String stringToEncode) {
		// encode string length
		encodeInt(bytes, stringToEncode.length());
		// encode string
		for (byte byteToAdd : stringToEncode.getBytes(StandardCharsets.UTF_8)) {
			bytes.add(byteToAdd);
		}
	}

}
