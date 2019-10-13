/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Utils
{
	public static byte[] INT_2_BYTE(Integer value)
	{
		return ByteBuffer.allocate(4).putInt(value.intValue()).array();
	}
	public static byte[] SHORT_2_BYTE(Short value)
	{
		return ByteBuffer.allocate(2).putShort(value.shortValue()).array();
	}
	public static byte[] LONG_2_BYTE(Long value)
	{
		return ByteBuffer.allocate(8).putLong(value.longValue()).array();
	}
	public static byte[] FLOAT_2_BYTE(Float value)
	{
		return ByteBuffer.allocate(4).putFloat(value.floatValue()).array();
	}
	public static byte[] DOUBLE_2_BYTE(Double value)
	{
		return ByteBuffer.allocate(8).putDouble(value.doubleValue()).array();
	}

	public static byte[] concatBytes(byte[]... arrays)
	{
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }

        byte[] result = new byte[length];

        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }		
        return result;
	}
	public static byte[] concatBytesArr(ArrayList<byte[]> arrays)
	{
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }

        byte[] result = new byte[length];

        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }		
        return result;
	}

	public static void reprBytes(byte[] bs, int size)
	{
		for(int i = 0; i < size; i++)
		{
			System.out.println(bs[i]);
		}
	}
	public static void main(String[] args)
	{
		Integer[] a = {1,2,3,4,4};
		byte[] b1 = INT_2_BYTE(a[2]);
		byte[] b2 = INT_2_BYTE(a[3]);
		byte[] b12 = concatBytes(b1, b2);
		ArrayList<byte[]> arr = new ArrayList<byte[]>();
		arr.add(b1);
		arr.add(b2);
		arr.add(b12);
		reprBytes(b12, b12.length);
		byte[] result = concatBytesArr(arr);
		reprBytes(result, result.length);
	}
}