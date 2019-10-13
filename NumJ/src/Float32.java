/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/

package org.NumJ

import java.nio.ByteBuffer;


class Float32 implements DType // float
{
	public float value;
	public Float32(byte b0, byte b1, byte b2, byte b3)
	{
		byte[] bytes = new byte{b0, b1, b2, b3};
		this.value = ByteBuffer.wrap(bytes).getFloat();
	}
	public byte[] toInt32()
	{
		int newVal = (int) this.value;
		return ByteBuffer.allocate(4).putInt(newVal).array();
	}
}