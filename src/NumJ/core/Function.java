/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
package NumJ.core;

import NumJ.type.BaseType;
import NumJ.type.Float64;
import NumJ.type.Float32;
import NumJ.type.Int32;
import NumJ.type.Int16;
import NumJ.type.Int64;

public class Function
{
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
		NDArray twos = NDArray.ones(dims, dtype, null).dot(2);
		NDArray exp = Function.exp(twos);
		NDArray log = Function.log(twos);
		NDArray abs = Function.abs(twos.dot(-1));
		NDArray poly = Function.poly(twos, 3);
		NDArray poly_neg = Function.poly_neg(twos, -1);
		exp.repr();	
		log.repr();
		abs.repr();
		poly.repr();
		poly_neg.repr();

	}
}