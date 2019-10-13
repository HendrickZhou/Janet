/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/

import java.nio.ByteBuffer;

class Float64 implements BaseType // float
{
	public double value;
	final static public int itemsize = 8; // byte size
	public Float64(){};
	public Float64(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7)
	{
		byte[] bytes = {b0, b1, b2, b3, b4, b5, b6, b7};
		this.value = ByteBuffer.wrap(bytes).getDouble();
	}

	public static byte[] toByte(Double value)
	{
		return Utils.DOUBLE_2_BYTE(value);
	}
	
	// public byte[] toInt32();
}