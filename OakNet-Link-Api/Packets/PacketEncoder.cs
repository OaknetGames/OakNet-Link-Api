using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api
	.Packets
{
	/// <summary>
	/// This class provides methods to encode types in byte arrays
	/// </summary>
	internal class PacketEncoder
	{
		/// <summary>
		/// Encodes a short in bytes and put them in an array
		/// <param name="data">The short to encode.</param>
		/// <param name="bytes">The byte array to put the encoded data.</param>
		/// </summary>
		internal static void EncodeShort(short data, ref byte[] bytes)
		{
			var oldData = bytes;
			bytes = new byte[bytes.Length + 2];
			Array.Copy(oldData, 0, bytes, 0, oldData.Length);
			bytes[bytes.Length - 2] = (byte)(data >> 8);
			bytes[bytes.Length - 1] = (byte)(data & 0b00000000_11111111);
		}

		/// <summary>
		/// Encodes a ushort in bytes and put them in an array
		/// <param name="data">The ushort to encode.</param>
		/// <param name="bytes">The byte array to put the encoded data.</param>
		/// </summary>
		internal static void EncodeUShort(ushort data, ref byte[] bytes)
		{
			var oldData = bytes;
			bytes = new byte[bytes.Length + 2];
			Array.Copy(oldData, 0, bytes, 0, oldData.Length);
			bytes[bytes.Length - 2] = (byte)(data >> 8);
			bytes[bytes.Length - 1] = (byte)(data & 0b00000000_11111111);
		}

		/// <summary>
		/// Encodes a short in bytes and put them in an array
		/// <param name="data">The int to encode.</param>
		/// <param name="bytes">The byte array to put the encoded data.</param>
		/// </summary>
		internal static void EncodeInt(int data, ref byte[] bytes)
		{
			var oldData = bytes;
			bytes = new byte[bytes.Length + 4];
			Array.Copy(oldData, 0, bytes, 0, oldData.Length);
			bytes[bytes.Length - 4] = (byte)(data >> 24);
			bytes[bytes.Length - 3] = (byte)((data >> 16) & 0b00000000_11111111);
			bytes[bytes.Length - 2] = (byte)((data >> 8) & 0b00000000_00000000_11111111);
			bytes[bytes.Length - 1] = (byte)(data & 0b00000000_00000000_00000000_11111111);
		}

		/// <summary>
		/// Encodes a char in bytes and put them in an array
		/// <param name="data">The char to encode.</param>
		/// <param name="bytes">The byte array to put the encoded data.</param>
		/// </summary>
		internal static void EncodeChar(char data, ref byte[] bytes)
		{
			EncodeUShort((ushort)data, ref bytes);
		}

		/// <summary>
		/// Encodes a string in bytes and put them in an array
		/// <param name="data">The string to encode.</param>
		/// <param name="bytes">The byte array to put the encoded data.</param>
		/// </summary>
		internal static void EncodeString(string data, ref byte[] bytes)
		{
            if (data == null)
                data = "";
			EncodeUShort((ushort)data.Length, ref bytes);
			foreach (var c in data)
			{
				EncodeChar(c, ref bytes);
			}
		}

		/// <summary>
		/// Encodes a byteArray and put them in an array
		/// <param name="data">The byte to encode.</param>
		/// <param name="bytes">The byte array to put the encoded data.</param>
		/// </summary>
		internal static void EncodeByteArray(byte[] data, ref byte[] bytes)
		{
			EncodeUShort((ushort)data.Length, ref bytes);
			var oldData = bytes;
			bytes = new byte[oldData.Length + data.Length];
			Array.Copy(oldData, 0, bytes, 0, oldData.Length);
			Array.Copy(data, 0, bytes, oldData.Length, data.Length);
		}
	}
}
