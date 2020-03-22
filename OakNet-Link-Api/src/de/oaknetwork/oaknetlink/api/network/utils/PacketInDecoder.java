package de.oaknetwork.oaknetlink.api.network.utils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import de.oaknetwork.oaknetlink.api.network.PacketException;

public class PacketInDecoder {

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

	public static short decodeShort(List<Byte> bytes) throws PacketException {
		if (bytes.size() < 2) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		short result = (short) ((bytes.get(2) << 8) | (bytes.get(3)));
		bytes.remove(0);
		bytes.remove(0);
		return result;
	}

	public static String decodeString(List<Byte> bytes) throws PacketException {

		// decode length
		int length = decodeInt(bytes);
		if (bytes.size() < length) {
			throw new PacketException("Can't decode Packet, expected more bytes than recieved");
		}
		// decode String
		byte[] stringBytes = new byte[length];
		for (int i = 0; i < length; i++) {
			stringBytes[i]=bytes.get(0);
			bytes.remove(0);
		}
		return new String(stringBytes, StandardCharsets.UTF_8);
	}
}
