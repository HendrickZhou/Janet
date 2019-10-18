/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
// package org.NumJ.core;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.Array;

public class Utils
{
	/**
	* General utils
	*
	*
	*
	**/
	static int[] deepCopyIntArray(int[] arr)
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
	static byte[] INT_2_BYTE(Integer value)
	{
		return ByteBuffer.allocate(4).putInt(value.intValue()).array();
	}
	static byte[] SHORT_2_BYTE(Short value)
	{
		return ByteBuffer.allocate(2).putShort(value.shortValue()).array();
	}
	static byte[] LONG_2_BYTE(Long value)
	{
		return ByteBuffer.allocate(8).putLong(value.longValue()).array();
	}
	static byte[] FLOAT_2_BYTE(Float value)
	{
		return ByteBuffer.allocate(4).putFloat(value.floatValue()).array();
	}
	static byte[] DOUBLE_2_BYTE(Double value)
	{
		return ByteBuffer.allocate(8).putDouble(value.doubleValue()).array();
	}

	static byte[] concatBytes(byte[]... arrays)
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
	static byte[] concatBytesArr(ArrayList<byte[]> arrays)
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
	* Array ops
	*
	*
	*/
	// static int[] getArrayShape(Object arr)
	// {
	// 	Class<?> arrClass = arr.getClass();
	// 	if(!arrClass.isArray())
	// 	{
	// 		throw new IllegalThreadStateException("input not a java array");
	// 	}
	// }

	static ArrayList<int[]> getAllIdxs(NDArray ndarr)
	{
		int numDims = ndarr.numDims;
		int seed = 0;
		ArrayList<int[]> idxs = new ArrayList<int[]>();
		boolean carry = false;
		int[] digs = _Int2Arr(seed, numDims);
		idxs.add(deepCopyIntArray(digs));
		for(int i = 1; i < ndarr.size; i++)
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

	/**
	* Representation
	* for high dimension data this operation is really expensive
	*
	*/

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

	/**
	* Use the String_builder to boost the speed
	*
	*/
	protected static String _repr(NDArray ndarr, boolean type_on)
	{
		int rowNum = ndarr.size / ndarr.shape[ndarr.numDims - 1];
		int numDims = ndarr.numDims;

		// get all the idx composition
		int seed = 0;
		ArrayList<int[]> idxs = new ArrayList<int[]>();
		boolean carry = false;
		int[] digs = _Int2Arr(seed, numDims);
		int[] changes = new int[rowNum];
		idxs.add(deepCopyIntArray(digs));
		changes[0] = numDims;
		for(int i = 1; i < ndarr.size; i++)
		{
			// define our addition here
			digs[digs.length - 1]++;
			int digs_changed = 0;
			for(int j = digs.length - 1; j >= 0; j--)
			{

				if(carry)
				{
					digs[j]++;
					digs_changed++;
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

			// check after visiting this row, how many new dimension are we crossing now
			if(i%ndarr.shape[numDims - 1] == 0)
			{
				changes[i/ndarr.shape[numDims- 1]] = digs_changed;
			}

			// after the addition, store the index, reset the flag
			idxs.add(deepCopyIntArray(digs));
			carry = false;
		}
		// System.out.println(Arrays.toString(changes));
		
		//generate sqaure bracket arrays and blank lines arrays
		String[] sqa_bracket_pre = new String[rowNum];
		String[] sqa_bracket_post = new String[rowNum];
		String[] blank_line = new String[rowNum]; // throw the first one
		for(int i = 0; i < rowNum; i++)
		{
			sqa_bracket_pre[i] = new String(new char[changes[i]]).replace("\0", "[");
			if(type_on)
			{
				if(i != 0)
				{
					sqa_bracket_pre[i] = new String(new char[numDims - changes[i] + 6]).replace("\0", " ").concat(sqa_bracket_pre[i]);
				}
				else
				{
					sqa_bracket_pre[i] = new String("array(").concat(sqa_bracket_pre[i]);
				}
				
			}
			else
			{
				sqa_bracket_pre[i] = new String(new char[numDims - changes[i]]).replace("\0", " ").concat(sqa_bracket_pre[i]);			
			}
		}
		for(int i = 0; i < rowNum; i++)
		{
			sqa_bracket_post[i] = new String(new char[changes[rowNum - i - 1]]).replace("\0", "]");
			if(type_on && (i == rowNum - 1))
			{
				sqa_bracket_post[i] = sqa_bracket_post[i].concat(String.format(",  dtype=%s)", ndarr.dtype.NAME));
			}					
		}
		for(int i = 0; i < rowNum; i++)
		{
			blank_line[i] = new String(new char[changes[i]]).replace("\0", "\n");
		}

		// System.out.println(Arrays.toString(sqa_bracket_pre));

		//start iteration
		String out_stream = "";
		out_stream = out_stream.concat(sqa_bracket_pre[0]);
		for(int i = 0; i < ndarr.size; i++)
		{
			if(i%ndarr.shape[numDims - 1] == (ndarr.shape[numDims - 1] - 1)) // last ele of this row
			{
				// out_stream = out_stream.concat(String.valueOf(ndarr.idx(idxs.get(i))));
				out_stream = out_stream.concat(String.valueOf(ndarr.dtype.parseByte(ndarr.DATA_POOL, i * ndarr.dtype.itemsize)));
				// out_stream = out_stream.concat(String.format("%-5d", ndarr.idx(idxs.get(i))));
				// System.out.println(String.valueOf(ndarr.idx(idxs.get(i))));
			}
			else
			{
				// out_stream = out_stream.concat(String.valueOf(ndarr.idx(idxs.get(i))) + ", ");
				out_stream = out_stream.concat(String.valueOf(ndarr.dtype.parseByte(ndarr.DATA_POOL, i * ndarr.dtype.itemsize)) + ", ");
				// out_stream = out_stream.concat(String.format("%-5d,", ndarr.idx(idxs.get(i))));
				// System.out.println(String.valueOf(ndarr.idx(idxs.get(i))));
			}

			// String ele = String.format("%-10d", );
			
			if((i+1)%ndarr.shape[numDims - 1] == 0)
			{
				out_stream = out_stream.concat(sqa_bracket_post[i/ndarr.shape[numDims - 1]]);
				if(i < (ndarr.size - 1))
				{
					out_stream = out_stream.concat(blank_line[i/ndarr.shape[numDims - 1] + 1]);
					out_stream = out_stream.concat(sqa_bracket_pre[(i+1)/ndarr.shape[numDims - 1]]);	
				}
			}
		}
		return out_stream;
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
		int[] dims = {2,3,4,5};
		DType t = new DType(new Int32());
		NDArray nd = new NDArray(dims, t, 'C');
		System.out.println(_repr(nd, true));
	}
}