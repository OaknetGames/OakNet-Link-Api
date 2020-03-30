package de.oaknetwork.oaknetlink.api.network.utils;

import java.nio.charset.StandardCharsets;

/**
 * This class provides useful methods to encodes primitives in a byte list.
 * 
 * All values are sent big endian.
 * 
 * @author Fabian Fila
 */
public class PacketOutEncoder {

	/**
	 * Encodes a long in a packetData
	 * 
	 * @param packetData   the packetData in which the encoded bytes will be put
	 * @param longToEncode the long which should be encoded
	 */
	public static void encodeLong(PacketData packetData, long longToEncode) {
		byte[] bytesToAdd = new byte[8];
		bytesToAdd[0] = (byte) ((longToEncode & 0xFF00000000000000l) >> 56);
		bytesToAdd[1] = (byte) ((longToEncode & 0x00FF000000000000l) >> 48);
		bytesToAdd[2] = (byte) ((longToEncode & 0x0000FF0000000000l) >> 40);
		bytesToAdd[3] = (byte) ((longToEncode & 0x000000FF00000000l) >> 32);
		bytesToAdd[4] = (byte) ((longToEncode & 0x00000000FF000000l) >> 24);
		bytesToAdd[5] = (byte) ((longToEncode & 0x0000000000FF0000l) >> 16);
		bytesToAdd[6] = (byte) ((longToEncode & 0x000000000000FF00l) >> 8);
		bytesToAdd[7] = (byte) (longToEncode & 0x00000000000000FFl);
		packetData.appendBytes(bytesToAdd);
	}

	/**
	 * Encodes an integer in a packetData
	 * 
	 * @param packetData  the packetData in which the encoded bytes will be put
	 * @param intToEncode the integer which should be encoded
	 */
	public static void encodeInt(PacketData packetData, int intToEncode) {
		byte[] bytesToAdd = new byte[4];
		bytesToAdd[0] = (byte) ((intToEncode & 0xFF000000) >> 24);
		bytesToAdd[1] = (byte) ((intToEncode & 0x00FF0000) >> 16);
		bytesToAdd[2] = (byte) ((intToEncode & 0x0000FF00) >> 8);
		bytesToAdd[3] = (byte) (intToEncode & 0x000000FF);
		packetData.appendBytes(bytesToAdd);
	}

	/**
	 * Encodes a short in a packetData
	 * 
	 * @param packetData    the packetData in which the encoded bytes will be put
	 * @param shortToEncode the short which should be encoded
	 */
	public static void encodeShort(PacketData packetData, short shortToEncode) {
		byte[] bytesToAdd = new byte[2];
		bytesToAdd[0] = (byte) ((shortToEncode & 0xFF00) >> 8);
		bytesToAdd[1] = (byte) (shortToEncode & 0x00FF);
		packetData.appendBytes(bytesToAdd);
	}

	/**
	 * Encodes a string in a packetData
	 * 
	 * @param packetData     the packetData in which the encoded bytes will be put
	 * @param stringToEncode the string which should be encoded
	 */
	public static void encodeString(PacketData packetData, String stringToEncode) {
		// encode string length
		encodeInt(packetData, stringToEncode.length());
		// encode string
		packetData.appendBytes(stringToEncode.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Encodes a BytePackage into a packetData
	 * 
	 * @param packetData  the packetData in which the encoded bytes will be put
	 * @param bytePackage
	 */
	public static void encodeBytePackage(PacketData packetData, BytePackage bytePackage) {
		// encode BytePackage length
		encodeInt(packetData, bytePackage.data.length);
		// encode BytePackage data
		packetData.appendBytes(bytePackage.data);
	}

}
