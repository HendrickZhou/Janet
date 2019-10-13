/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
import java.nio.ByteBuffer;

class Int32 implements DType // int
{
	public int value;
	public Int32(byte b0, byte b1, byte b2, byte b3)
	{
		this.value = b3 + (b2<<8) + (b1<<16) + (b0<<24);
	}
	public byte[] toFloat32()
	{
		float newVal = (float) this.value;
		return ByteBuffer.allocate(4).putFloat(newVal).array();
	}

	public static void main(String[] argv)
	{
		int testInt = 40;
		byte t = (byte) testInt;
		System.out.println(t);
		// System.out.println(t[0]);
		// System.out.println(t[1]);
		// System.out.println(t[2]);
		// System.out.println(t[3]);

		byte[] bs = ByteBuffer.allocate(4).putInt(testInt).array();

		System.out.println(bs[0]);
		System.out.println(bs[1]);
		System.out.println(bs[2]);
		System.out.println(bs[3]);

		Int32 test = new Int32(bs[0], bs[1], bs[2], bs[3]);

		System.out.println(test.value);

	}
}