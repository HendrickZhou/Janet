/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/

import java.nio.ByteBuffer;

class Int16 implements BaseType // float
{
	public short value;
	final static public int itemsize = 2; // byte size
	public Int16(){};
	public Int16(byte b0, byte b1)
	{
		byte[] bytes = {b0, b1};
		this.value = ByteBuffer.wrap(bytes).getShort();
	}

	public static byte[] toByte(Short value)
	{
		return Utils.SHORT_2_BYTE(value);
	}
	
	// public byte[] toInt32()
}