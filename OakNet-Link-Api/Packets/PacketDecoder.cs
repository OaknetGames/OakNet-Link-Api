using System;
using System.Collections.Generic;
using System.Text;

namespace OakNetLink.Api.Packets
{
	/// <summary>
	/// This class provides methods to decode types from byte arrays
	/// </summary>
	internal class PacketDecoder
    {
		/// <summary>
		/// Decodes a short from a byte array and removes the used bytes
		/// <param name="bytes">The byte array to decode.</param>
		/// </summary>
		internal static short DecodeShort(ref byte[] bytes)
        {
            short result=(short)(bytes[0]<<8|bytes[1]);
            var oldBytes=bytes;
            bytes = new byte[bytes.Length - 2];
            Array.Copy(oldBytes, 2, bytes, 0, bytes.Length);
            return result;
        }

		/// <summary>
		/// Decodes a ushort from a byte array and removes the used bytes
		/// <param name="bytes">The byte array to decode.</param>
		/// </summary>
		internal static ushort DecodeUShort(ref byte[] bytes)
        {
            ushort result=(ushort)(bytes[0]<<8|bytes[1]);
            var oldBytes=bytes;
            bytes = new byte[bytes.Length - 2];
            Array.Copy(oldBytes, 2, bytes, 0, bytes.Length);
            return result;
        }

		/// <summary>
		/// Decodes an int from a byte array and removes the used bytes
		/// <param name="bytes">The byte array to decode.</param>
		/// </summary>
		internal static int DecodeInt(ref byte[] bytes)
		{
			int result = bytes[0] << 24 | bytes[1] << 16 | bytes[2] << 8 | bytes[3];
			var oldBytes = bytes;
			bytes = new byte[bytes.Length - 4];
			Array.Copy(oldBytes, 4, bytes, 0, bytes.Length);
			return result;
		}

		/// <summary>
		/// Decodes a char from a byte array and removes the used bytes
		/// <param name="bytes">The byte array to decode.</param>
		/// </summary>
		internal static char DecodeChar(ref byte[] bytes)
		{
			return (char)DecodeUShort(ref bytes);
		}

		/// <summary>
		/// Decodes a string from a byte array and removes the used bytes
		/// <param name="bytes">The byte array to decode.</param>
		/// </summary>
		internal static string DecodeString(ref byte[] bytes)
		{
			ushort length = DecodeUShort(ref bytes);
			var result = "";
			for (var i = 0; i < length; i++)
			{
				result += DecodeChar(ref bytes);
			}
			return result;
		}

		/// <summary>
		/// Decodes a byte Array from a byte array and removes the used bytes
		/// <param name="bytes">The byte array to decode.</param>
		/// </summary>
		internal static byte[] DecodeByteArray(ref byte[] bytes)
		{
			ushort length = DecodeUShort(ref bytes);
			var result = new byte[length];
			Array.Copy(bytes, 0, result, 0, result.Length);
			var oldBytes = bytes;
			bytes = new byte[bytes.Length - length];
			Array.Copy(oldBytes, length, bytes, 0, bytes.Length);
			return result;
		}
	}
}
