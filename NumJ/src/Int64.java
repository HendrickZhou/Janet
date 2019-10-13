/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/

import java.nio.ByteBuffer;

class Int64 implements BaseType // float
{
	public long value;
	final static public int itemsize = 8; // byte size
	public Int64(){};
	public Int64(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7)
	{
		byte[] bytes = {b0, b1, b2, b3, b4, b5, b6, b7};
		this.value = ByteBuffer.wrap(bytes).getLong();
	}

	public static byte[] toByte(Long value)
	{
		return Utils.LONG_2_BYTE(value);
	}
	
	// public byte[] toInt32();
}