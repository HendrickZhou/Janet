/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
package NumJ.core;

import NumJ.type.*;
import NumJ.math.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Function
{
	/**********************
	custom operations:
	2 possible ways to do: 
	@ implementing all the operations for all type
	@ Operate on the bytes(likely faster)
	But the note only the first one is for public use
	The second way is for other packages user that embed the NumJ
	**********************/

	// the better way to do this is define your own function
	// with the filter condtion embedded but this is a more 
	// convenient way to do so.
	// Note we can offer generic method here. But it's not necessary so far
	// you can complete the support for all types later
	// return indexs here
	// more like a toy function here.
	public static ArrayList<int[]> where(Filter_int filterRef, NDArray arr)
	{
		ArrayList<int[]> arr_idxs = Utils.getAllIdxs(arr);
		ArrayList<int[]> idxs = new ArrayList<int[]>();
		for(int[] idx : arr_idxs)
		{
			int val = arr.idx_int(idx);
			if(filterRef.selected(val))
			{
				idxs.add(idx);
			}
		}
		return idxs;
	}
	public static ArrayList<int[]> where(Filter_double filterRef, NDArray arr)
	{
		ArrayList<int[]> arr_idxs = Utils.getAllIdxs(arr);
		ArrayList<int[]> idxs = new ArrayList<int[]>();
		for(int[] idx : arr_idxs)
		{
			double val = arr.idx_double(idx);
			if(filterRef.selected(val))
			{
				idxs.add(idx);
			}
		}
		return idxs;
	}

	// this one operate on bytes directlly.
	// inline operations
	// be aware of 2 things if user make the mistake:
	// the my_func might not receive the byte array with right length, this could cause the OutOfIndex exception
	// the length is right but the way user handle the type is wrong(operate float with int eg.), the output could be 
	// out of control, but the program wont crash
	//
	// we support all type theoratically, 
	// but for sure all the result will be converted to Float64!!!!
	protected static void each_do(MyFunc my_func, NDArray arr)
	{
		byte[] DATA_DOUBLE = new byte[arr.size * 8];
		int item_size = arr.dtype.itemsize;
		byte[] old_byte = new byte[item_size];
		byte[] new_byte = new byte[8];
		for(int i = 0; i < arr.size; i++)
		{
			for(int j = 0; j < item_size; j++)
			{
				old_byte[j] = arr.DATA_POOL[i*item_size + j];				
			}
			new_byte = my_func.doMath(old_byte);
			if(new_byte.length != 8)
			{
				throw new IllegalArgumentException("The lambda function should return 8 bytes array!!!");
			}
			for(int j = 0; j < 8; j++)
			{
				DATA_DOUBLE[i*8 + j] = new_byte[j];			
			}
		}
		arr.setter_DATAPOOL(DATA_DOUBLE);
		Float64 f = new Float64();
		DType type = new DType(f);
		arr.setter_dtype(type);
		arr.setter_shape(arr.getter_shape());
	}
	// in-place for array 1
	protected static void each_pair_do(MyPairFunc my_func, NDArray arr1, NDArray arr2)
	{
		if(!Arrays.equals(arr1.shape, arr2.shape))
		{
			throw new IllegalArgumentException("unmatched shape");
		}
		byte[] DATA_DOUBLE = new byte[arr1.size * 8];
		int item_size_1 = arr1.dtype.itemsize;
		int item_size_2 = arr2.dtype.itemsize;
		byte[] old_byte_1 = new byte[item_size_1];
		byte[] old_byte_2 = new byte[item_size_2];
		byte[] new_byte = new byte[8];
		for(int i = 0; i < arr1.size; i++)
		{
			for(int j = 0; j < item_size_1; j++)
			{
				old_byte_1[j] = arr1.DATA_POOL[i*item_size_1 + j];				
			}
			for(int j = 0; j < item_size_2; j++)
			{
				old_byte_2[j] = arr2.DATA_POOL[i*item_size_2 + j];				
			}			
			new_byte = my_func.doMath(old_byte_1, old_byte_2);
			if(new_byte.length != 8)
			{
				throw new IllegalArgumentException("The lambda function should return 8 bytes array!!!");
			}
			for(int j = 0; j < 8; j++)
			{
				DATA_DOUBLE[i*8 + j] = new_byte[j];			
			}
		}
		arr1.setter_DATAPOOL(DATA_DOUBLE);
		Float64 f = new Float64();
		DType type = new DType(f);
		arr1.setter_dtype(type);
		arr1.setter_shape(arr1.getter_shape());
	}


	protected static NDArray exp(NDArray arr)
	{
		// should check type
		if(!(arr.dtype.NAME.equals("NumJ.Int32") || arr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		}
		NDArray newarr = NDArray.deepCopy(arr);	

		if(arr.dtype.NAME.equals("NumJ.Int32"))
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.exp(arr.dtype.parseByteInt(arr.DATA_POOL, i*4));
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
			}		
		}
		else
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.exp(arr.dtype.parseByteDouble(arr.DATA_POOL, i*8));
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
			}		
		}	
		return newarr;		
	}

	// 
	protected static NDArray log(NDArray arr)
	{
		// should check type
		if(!(arr.dtype.NAME.equals("NumJ.Int32") || arr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		}
		NDArray newarr = NDArray.deepCopy(arr);	

		if(arr.dtype.NAME.equals("NumJ.Int32"))
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.log(arr.dtype.parseByteInt(arr.DATA_POOL, i*4));
				if (Double.isNaN(newval))
				{
					// System.out.println("Warning! the array will be damaged!")
					throw new IllegalArgumentException("The array can't have negtive or 0 Number");
				}
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
			}		
		}
		else
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.log(arr.dtype.parseByteDouble(arr.DATA_POOL, i*8));
				if (Double.isNaN(newval))
				{
					// System.out.println("Warning! the array will be damaged!")
					throw new IllegalArgumentException("The array can't have negtive or 0 Number");
				}
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
			}		
		}	
		return newarr;	
	}

	protected static NDArray abs(NDArray arr)
	{
		// should check type
		if(!(arr.dtype.NAME.equals("NumJ.Int32") || arr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		}
		NDArray newarr = NDArray.deepCopy(arr);	

		if(arr.dtype.NAME.equals("NumJ.Int32"))
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.abs(arr.dtype.parseByteInt(arr.DATA_POOL, i*4));
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
			}		
		}
		else
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.abs(arr.dtype.parseByteDouble(arr.DATA_POOL, i*8));
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
			}		
		}	
		return newarr;	
	}

	protected static NDArray poly_neg(NDArray arr, double d)
	{
		if(d >= 0)
		{
			throw new IllegalArgumentException("Use poly instead");
		}
		NDArray newarr = NDArray.deepCopy(arr);	

		if(arr.dtype.NAME.equals("NumJ.Int32"))
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.pow(1f/arr.dtype.parseByteInt(arr.DATA_POOL, i*4), -d);
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
			}		
		}
		else
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.pow(1f/arr.dtype.parseByteDouble(arr.DATA_POOL, i*8), -d);
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
			}		
		}	
		return newarr;		
	}
	protected static NDArray poly(NDArray arr, double d)
	{
		if(d < 0)
		{
			throw new IllegalArgumentException("don't support negative poly");
		}
		// should check type
		if(!(arr.dtype.NAME.equals("NumJ.Int32") || arr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		}
		NDArray newarr = NDArray.deepCopy(arr);	

		if(arr.dtype.NAME.equals("NumJ.Int32"))
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.pow(arr.dtype.parseByteInt(arr.DATA_POOL, i*4), d);
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
			}		
		}
		else
		{
			Float64 F = new Float64();
			DType t = new DType(F);
			newarr.setter_dtype(t);
			byte[] new_dp = new byte[arr.size *8];
			newarr.setter_shape(arr.getter_shape());
			newarr.setter_DATAPOOL(new_dp);
			for(int i = 0; i<arr.size; i++)
			{
				double newval = Math.pow(arr.dtype.parseByteDouble(arr.DATA_POOL, i*8), d);
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					newarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
				}				
		}		
		}	
		return newarr;	
	}

	public static void main(String [] args)
	{
				// System.out.println(i);
		Int32 type = new Int32();
		DType dtype = new DType(type);
		int[] dims = {2,3};
		NDArray rand = new NDArray(dims, dtype, null);
		NDArray twos = NDArray.ones(dims, dtype, null).dot(2);
		NDArray exp = Function.exp(twos);
		NDArray log = Function.log(twos);
		NDArray abs = Function.abs(twos.dot(1.0));
		NDArray poly = Function.poly(twos, 3);
		NDArray poly_neg = Function.poly_neg(twos, -1);
		exp.repr();	
		log.repr();
		abs.repr();
		poly.repr();
		poly_neg.repr();
		twos.repr();

		rand.repr();
		Filter_int filterRef = (val) -> val%2 == 0 ? false : true;
		ArrayList<int[]> idxs = Function.where(filterRef, rand);
		for(int[] idx : idxs)
		{
			System.out.println(Arrays.toString(idx));
		}

		MyFunc sigmod = (bytes) ->{
			double val = DType.parseByteDouble(bytes, 0);
			return DType.toByteAuto(1.0 / (1.0 + Math.exp(-val)));
		};

		MyPairFunc mul = (bytes1, bytes2) ->{
			double val_1 = DType.parseByteDouble(bytes1, 0);
			int val_2 = DType.parseByteInt(bytes2, 0);
			return DType.toByteAuto(val_1*val_2);
		};

		// Function.each_do(sigmod, twos);
		twos.repr();
		abs.repr();

		Function.each_pair_do(mul, abs, twos);
		abs.repr();

	}
}