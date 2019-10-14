/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
import java.nio.ByteBuffer;

class Int32 implements BaseType // int
{
	final static public String NAME = "NumJ.Int32";
	final static public int itemsize = 4; // byte size
	public String getNAME()
	{
		return this.NAME;
	}
	public int getitemsize()
	{
		return this.itemsize;
	}

	
	// public byte[] toFloat32()
	// {
	// 	float newVal = (float) this.value;
	// 	return ByteBuffer.allocate(4).putFloat(newVal).array();
	// }

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
		// int testInt = 40;
		// byte t = (byte) testInt;
		// System.out.println(t);
		// System.out.println(t[0]);
		// System.out.println(t[1]);
		// System.out.println(t[2]);
		// System.out.println(t[3]);

		// byte[] bs = ByteBuffer.allocate(4).putInt(testInt).array();

		// System.out.println(bs[0]);
		// System.out.println(bs[1]);
		// System.out.println(bs[2]);
		// System.out.println(bs[3]);

		// int test = fromByte(bs[0], bs[1], bs[2], bs[3]);

		// System.out.println(test);

		// Integer[] arr = {1,2,3};
		// test(arr);

	}
}