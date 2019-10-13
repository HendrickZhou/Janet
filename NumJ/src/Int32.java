/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
import java.nio.ByteBuffer;

class Int32 implements BaseType // int
{
	public int value;
	final static public int itemsize = 4; // byte size

	public Int32(){};
	
	public Int32(byte b0, byte b1, byte b2, byte b3)
	{
		this.value = b3 + (b2<<8) + (b1<<16) + (b0<<24);
	}
	
	public static byte[] toByte(Integer value)
	{
		return Utils.INT_2_BYTE(value);
	}
	
	public byte[] toFloat32()
	{
		float newVal = (float) this.value;
		return ByteBuffer.allocate(4).putFloat(newVal).array();
	}

	// public static <N extends Number> void test(N[] arr)
	// {
	// 	String name = arr[0].getClass().getName().substring(10);

	// 	System.out.println(name.length());
	// 	System.out.println(name);
	// 	if(arr[0].getClass().getSimpleName().equals("Integer"))
	// 	{
	// 		System.out.println(arr[0].getClass().getName());
	// 		System.out.println(arr[0].intValue());
	// 		System.out.println(arr[1].intValue());
	// 		System.out.println(arr[2].intValue());
	// 	}
	// }

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

		// Integer[] arr = {1,2,3};
		// test(arr);

	}
}