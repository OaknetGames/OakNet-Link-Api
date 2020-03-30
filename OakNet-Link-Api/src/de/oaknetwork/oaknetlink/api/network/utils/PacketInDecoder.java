package de.oaknetwork.oaknetlink.api.network.utils;

import java.nio.charset.StandardCharsets;
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
	 * Decodes a long out of a byte list. The 8 bytes used for the decode will be
	 * removed
	 * 
	 * @param bytes byte list to decode from
	 * @return the decoded long
	 * @throws PacketException
	 */
	public static long decodeLong(PacketData packetData) throws PacketException {
		if (packetData.data.length < 8) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		long result = 0x0000000000000000L;
		result |=  0xFF00000000000000L & (((long) packetData.data[0]) << 56);
		result |=  0x00FF000000000000L & (((long) packetData.data[1]) << 48);
		result |=  0x0000FF0000000000L & (((long) packetData.data[2]) << 40);
		result |=  0x000000FF00000000L & (((long) packetData.data[3]) << 32);
		result |=  0x00000000FF000000L & (((long) packetData.data[4]) << 24);
		result |=  0x0000000000FF0000L & (((long) packetData.data[5]) << 16);
		result |=  0x000000000000FF00L & (((long) packetData.data[6]) << 8);
		result |=  0x00000000000000FFL & ((long) packetData.data[7]);
		packetData.removeBytes(8);
		return result;
	}

	/**
	 * Decodes an integer out of a byte list. The 4 bytes used for the decode will
	 * be removed
	 * 
	 * @param bytes byte list to decode from
	 * @return the decoded integer
	 * @throws PacketException
	 */
	public static int decodeInt(PacketData packetData) throws PacketException {
		if (packetData.data.length < 4) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		int result = (packetData.data[0] << 24) | (packetData.data[1] << 16) | (packetData.data[2] << 8)
				| (packetData.data[3]);
		packetData.removeBytes(4);
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
	public static short decodeShort(PacketData packetData) throws PacketException {
		if (packetData.data.length < 2) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		short result = (short) ((packetData.data[0] << 8) | (packetData.data[1]));
		packetData.removeBytes(2);
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
	public static String decodeString(PacketData packetData) throws PacketException {

		// decode length
		int length = decodeInt(packetData);
		if (packetData.data.length < length) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		// decode String
		byte[] stringBytes = new byte[length];
		System.arraycopy(packetData.data, 0, stringBytes, 0, stringBytes.length);
		packetData.removeBytes(length);
		return new String(stringBytes, StandardCharsets.UTF_8);
	}

	/**
	 * Decodes a BytePackage out of a byte list. The bytes used for the decode will
	 * be removed
	 * 
	 * @param bytes byte list to decode from
	 * @return the decoded BytePackage
	 * @throws PacketException
	 */
	public static BytePackage decodeBytePackage(PacketData packetData) throws PacketException {

		// decode length
		int length = decodeInt(packetData);
		if (packetData.data.length < length) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		// decode BytePackage
		byte[] bytePackageBytes = new byte[length];
		System.arraycopy(packetData.data, 0, bytePackageBytes, 0, bytePackageBytes.length);
		packetData.removeBytes(length);

		return new BytePackage(bytePackageBytes);
	}
}
