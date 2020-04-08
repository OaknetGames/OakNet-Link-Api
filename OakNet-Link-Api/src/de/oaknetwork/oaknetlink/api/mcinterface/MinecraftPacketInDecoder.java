package de.oaknetwork.oaknetlink.api.mcinterface;

import java.io.IOException;
import java.io.InputStream;

import de.oaknetwork.oaknetlink.api.network.utils.PacketData;
import de.oaknetwork.oaknetlink.api.utils.Tuple;

/**
 * Ported from the first OakNet-Link version
 * 
 * @author Fabian Fila
 */
public class MinecraftPacketInDecoder {
	
	/** 
	 * Decodes an Integer with variable size.
	**/
	public static Tuple<Integer, PacketData> decodeVarInt(InputStream istream) throws IOException {
		int numRead = 0;
		int result = 0;
		byte read;
		int tempRead;
		PacketData packetData = new PacketData();
		packetData.data = new byte[0];
		do {
			
			tempRead = (byte) istream.read();
			if(tempRead == -1)
				throw new IOException("Socket closed");
			read = (byte) tempRead;
			packetData.appendBytes(read);
			int value = (read & 0b01111111);
			result |= (value << (7 * numRead));

			numRead++;
			if (numRead > 5) {
				throw new RuntimeException("VarInt is too big");
			}
		} while ((read & 0b10000000) != 0);
		return new Tuple<Integer, PacketData>(result, packetData);
	}
}
