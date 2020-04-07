package de.oaknetwork.oaknetlink.api.mcinterface;

import java.io.IOException;
import java.io.InputStream;

/**
 * Ported from the first OakNet-Link version
 * 
 * @author Fabian Fila
 */
public class MinecraftPacketInDecoder {
	
	/** Decodes an Integer with variable size.
	** IN: InputStream 
	** OUT: Int-Array; First index is the decoded int, Second is how many bytes were used
	**/
	public static int decodeVarInt(InputStream istream) throws IOException {
		int numRead = 0;
		int result = 0;
		byte read;
		do {
			
			read = (byte) istream.read();
			int value = (read & 0b01111111);
			result |= (value << (7 * numRead));

			numRead++;
			if (numRead > 5) {
				throw new RuntimeException("VarInt is too big");
			}
		} while ((read & 0b10000000) != 0);
		return result;
	}
}
