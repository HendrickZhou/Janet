/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/

import java.nio.ByteBuffer;

class Float32 implements BaseType // float
{
	public float value;
	final static public int itemsize = 4; // byte size
	public Float32(){};
	public Float32(byte b0, byte b1, byte b2, byte b3)
	{
		byte[] bytes = {b0, b1, b2, b3};
		this.value = ByteBuffer.wrap(bytes).getFloat();
	}

	public static byte[] toByte(Float value)
	{
		return Utils.FLOAT_2_BYTE(value);
	}
	
	public byte[] toInt32()
	{
		int newVal = (int) this.value;
		return ByteBuffer.allocate(4).putInt(newVal).array();
	}
}