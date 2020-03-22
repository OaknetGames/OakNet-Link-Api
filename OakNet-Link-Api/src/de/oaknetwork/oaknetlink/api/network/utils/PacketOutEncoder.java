package de.oaknetwork.oaknetlink.api.network.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class PacketOutEncoder {

	// All values are sent big endian

	public static void encodeInt(List<Byte> bytes, int intToEncode) {
		bytes.add((byte) ((intToEncode & 0xFF000000) >> 24));
		bytes.add((byte) ((intToEncode & 0x00FF0000) >> 16));
		bytes.add((byte) ((intToEncode & 0x0000FF00) >> 8));
		bytes.add((byte) (intToEncode & 0x000000FF));
	}

	public static void encodeShort(List<Byte> bytes, short shortToEncode) {
		bytes.add((byte) ((shortToEncode & 0xFF00) >> 8));
		bytes.add((byte) (shortToEncode & 0x00FF));
	}

	public static void encodeString(List<Byte> bytes, String stringToEncode) {
		// encode string length
		encodeInt(bytes, stringToEncode.length());
		// encode string
		for (byte byteToAdd : stringToEncode.getBytes(StandardCharsets.UTF_8)) {
			bytes.add(byteToAdd);
		}
	}

}
