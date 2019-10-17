/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils
{
	/**
	* General utils
	*
	*
	*
	**/
	public static int[] deepCopyIntArray(int[] arr)
	{
		int[] new_arr = new int[arr.length];
		for(int i = 0; i < arr.length; i++)
		{
			new_arr[i] = arr[i];
		}
		return new_arr;
	}



	/**
	* Byte opeartions
	*
	*
	*
	*/
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
        for (byte[] array : arrays) 
        {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }		
        return result;
	}

	/**
	* Representation
	* for high dimension data this operation is really expensive
	*
	*/
	// public static void reprBytesArr(byte[] bs, int size)
	// {
	// 	String r = new String("[");
	// 	for(int i = 0; i < size; i++)
	// 	{
	// 		r += new String(bs[i]);
	// 		r += ", ";
	// 	}
	// 	System.out.println(r);
	// }

	// public static void reprIntArr(int[] arr)
	// {
	// 	String r = new String("[");
	// 	for(int i = 0; i < arr.length; i++)
	// 	{
	// 		r += new String(arr[i]);
	// 		r += ", ";
	// 	}
	// 	r += "]";
	// 	System.out.println(r);
	// }

	// small endian pair
	private static int[] _Int2Arr(int val, int len)
	{
		int[] digits = new int[len]; // all zeros
		int i = len - 1;
		while(val > 0)
		{
			digits[i--] = val % 10;
			val /= 10;
		}
		return digits;
	}

	private static int _Arr2Int(int[] arr)
	{
		int len = arr.length;
		int sum = 0;
		int base = 1;
		for(int i = len - 1; i >= 0; i--)
		{
			sum += arr[i] * base;
			base *= 10;
		}
		return sum;
	}

	protected static ArrayList<int[]> _repr(NDArray ndarr)
	{
		int rowNum = ndarr.size / ndarr.shape[ndarr.numDims - 1];
		// String
		String pre = new String(new char[ndarr.numDims]).replace("\0", "[");
		String post = new String(new char[ndarr.numDims]).replace("\0", "]");

		// get all the idx composition
		int seed = 0;
		ArrayList<int[]> idxs = new ArrayList<int[]>();
		boolean carry = false;
		int[] digs = _Int2Arr(seed, ndarr.numDims);
		idxs.add(deepCopyIntArray(digs));
		for(int i = 0; i < ndarr.size-1; i++)
		{
			// define our addition here
			digs[digs.length - 1]++;
			for(int j = digs.length - 1; j >= 0; j--)
			{
				if(carry)
				{
					digs[j]++;
				}
				if(digs[j] >= ndarr.shape[j])
				{
					digs[j] = 0;
					carry = true;
				}
				else
				{
					carry = false;
					break;
				}
			}

			// after the addition, store the index, reset the flag
			idxs.add(deepCopyIntArray(digs));
			carry = false;
		}
		return idxs;
	}







	public static void main(String[] args)
	{
		// Integer[] a = {1,2,3,4,4};
		// byte[] b1 = INT_2_BYTE(a[2]);
		// byte[] b2 = INT_2_BYTE(a[3]);
		// byte[] b12 = concatBytes(b1, b2);
		// ArrayList<byte[]> arr = new ArrayList<byte[]>();
		// arr.add(b1);
		// arr.add(b2);
		// arr.add(b12);
		// reprBytesArr(b12, b12.length);
		// byte[] result = concatBytesArr(arr);
		// reprBytesArr(result, result.length);
		// int[] r = _Int2Arr(250, 4);
		// reprIntArr(r);
		// int rr = _Arr2Int(r);
		// System.out.println(rr);

		// test addtion
		int[] dims = {2,3,4};
		DType t = new DType(new Int32());
		NDArray nd = new NDArray(dims, t, 'C');
		ArrayList<int[]> idxs = _repr(nd);
		for(int[] idx : idxs)
		{
			System.out.println(Arrays.toString(idx));
		}
	}
}