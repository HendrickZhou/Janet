/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
package NumJ.core;

import NumJ.type.*;
import NumJ.math.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
* This class only handle matrix operation with 2 or lower dimensions.
*
*
*/
class Matrix
{
	protected static NDArray hstack(NDArray arr1, NDArray arr2) // stack horizontally(col wise)
	{
		if(arr1.shape.length != 2 || arr2.shape.length != 2)
		{
			throw new IllegalArgumentException("not a matrix");	
		}
		if(arr1.shape[0] != arr2.shape[0])
		{
			throw new IllegalArgumentException("unaligned col number");
		}
		if(!arr1.dtype.NAME.equals(arr2.dtype.NAME))
		{
			throw new IllegalArgumentException("different type not supported");
		}

		int[] shape = {arr1.shape[0], arr1.shape[1] + arr2.shape[1]};
		NDArray concat = NDArray.zeros(shape, arr1.dtype, 'C');
		ArrayList<int[]> idxs = Utils.getAllIdxs(concat);
		for(int[] idx : idxs)
		{
			if(idx[1] - arr1.shape[1] + 1 > 0)
			{
				int old_col = idx[1] - arr1.shape[1];
				int[] old_idx = {idx[0], old_col};
				byte[] old_bytes = arr2._idx_byte(old_idx);
				int offset = concat._idx(idx);
				for(int j = 0; j < arr1.dtype.itemsize; j++)
				{
					concat.modify_DATAPOOL(offset + j, old_bytes[j]);	
				}			
			}
			else
			{
				int old_col = idx[1];
				int[] old_idx = {idx[0], old_col};
				byte[] old_bytes = arr1._idx_byte(old_idx);
				int offset = concat._idx(idx);
				for(int j = 0; j < arr1.dtype.itemsize; j++)
				{
					concat.modify_DATAPOOL(offset + j, old_bytes[j]);	
				}			
			}
		}
		return concat;

	}
	protected static NDArray vstack(NDArray arr1, NDArray arr2) // stack vertically(row wise)
	{
		if(arr1.shape[1] != arr2.shape[1])
		{
			throw new IllegalArgumentException("unaligned col number");
		}
		if(!arr1.dtype.NAME.equals(arr2.dtype.NAME))
		{
			throw new IllegalArgumentException("different type not supported");
		}
		int[] shape = {arr1.shape[0] + arr2.shape[0], arr1.shape[1]};
		NDArray concat = NDArray.zeros(shape, arr1.dtype, 'C');
		ArrayList<int[]> idxs = Utils.getAllIdxs(concat);
		for(int[] idx : idxs)
		{
			if(idx[0] - arr1.shape[0] + 1 > 0)
			{
				int old_row = idx[0] - arr1.shape[0];
				int[] old_idx = {old_row, idx[1]};
				byte[] old_bytes = arr2._idx_byte(old_idx);
				int offset = concat._idx(idx);
				for(int j = 0; j < arr1.dtype.itemsize; j++)
				{
					concat.modify_DATAPOOL(offset + j, old_bytes[j]);	
				}			
			}
			else
			{
				int old_row = idx[0];
				int[] old_idx = {old_row, idx[1]};
				byte[] old_bytes = arr1._idx_byte(old_idx);
				int offset = concat._idx(idx);
				for(int j = 0; j < arr1.dtype.itemsize; j++)
				{
					concat.modify_DATAPOOL(offset + j, old_bytes[j]);	
				}			
			}
		}
		return concat;

	}
	// wont change original, not in-place
	protected static NDArray T(NDArray ndarr)
	{
		if(ndarr.numDims > 2)
		{
			throw new IllegalArgumentException("can't support transpose over 2d");
		}
		if(ndarr.numDims == 1)
		{
			return NDArray.deepCopy(ndarr);
		}
		int rowNum = ndarr.shape[0];
		int colNum = ndarr.shape[1];
		NDArray newarr = NDArray.deepCopy(ndarr);
		int [] new_shape = {colNum, rowNum};
		newarr.setter_shape(new_shape);

		int[] newidx = new int[2];
		int[] oldidx = new int[2];
        for(int i = 0; i < rowNum; i++)
        {
        	for(int j = 0; j < colNum; j++)
        	{
        		oldidx[0] = i;
        		oldidx[1] = j;
        		newidx[0] = j;
        		newidx[1] = i;

        		for(int k = 0; k < ndarr.dtype.itemsize; k++)
        		{
	        		newarr.modify_DATAPOOL(newarr._idx(newidx) + k, ndarr.DATA_POOL[ndarr._idx(oldidx) + k]);	
        		}
        		
        		
        	}
        }
        return newarr;
	}


	// this is a simplified sum!! 
	// only works for matrix
	// and 2 axis
	// 0 sum over row : (m, n) -> (1, n)
	// 1 sum over col : (m, n) -> (m ,1)
	protected static NDArray matsum(NDArray ndarr, int axis, boolean keep_d)
	{
		if(ndarr.shape.length != 2)
		{
			throw new IllegalArgumentException("Dub, matrix plz!");
		}
		int row = ndarr.shape[0];
		int col = ndarr.shape[1];
		switch(axis)
		{
			case 0:
			{
				int[] sum_shape = {col};
				NDArray sum = NDArray.zeros(sum_shape, ndarr.dtype, 'C');
				for(int r = 0; r < row; r++)
				{
					NDArray arr_row = Matrix.getRow(ndarr, r);
					sum = Matrix.matadd(sum, arr_row);
				}
				if(keep_d)
				{
					int[] k_d = {1, col};
					sum.reshape(k_d);
					return sum;
				}
				return sum;
			}
			case 1:
			{
				int[] sum_shape = {row};
				NDArray sum = NDArray.zeros(sum_shape, ndarr.dtype, 'C');
				for(int c = 0; c < col; c++)
				{
					NDArray arr_col = Matrix.getCol(ndarr, c);
					sum = Matrix.matadd(sum, arr_col);
				}
				if(keep_d)
				{
					int[] k_d = {row, 1};
					sum.reshape(k_d);
					return sum;
				}
				return sum;
			}
			default:
				throw new IllegalArgumentException("use 1 or 0 as axis");
		}
	}
	// we do not handle uncertain dimension array here
	// Since the type conversion haven't been finished yet, all the operation are assumed
	// to be the same data type
	// but since we're using the java's default number operation type casting, we'll not check the 
	// type correctness here as this part actually has some flexibility in type
	//
	// ndarr can only be int type or double type
	protected static void scalar(NDArray ndarr, int scalar)
	{
		// should check type
		if(!(ndarr.dtype.NAME.equals("NumJ.Int32") || ndarr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		}
		// NDArray newarr = NDArray.deepCopy(ndarr); // newarr is a int32 array
		ArrayList<int[]> idxs = Utils.getAllIdxs(ndarr);
        Iterator<int[]> iter 
            = idxs.iterator();
        int [] idx;
        byte[] newvalbyte;

        switch(ndarr.dtype.NAME)
        {
        	case "NumJ.Int32":
        	{
		        int newval;
				for(int i = 0; i < ndarr.size; i++)
				{
					// try
					// {
					// 	idx = iter.next();	
					// }
					// catch(java.util.NoSuchElementException e)
					// {
					// 	System.out.println("inner wrong array size"); // not expect it to happen;
					// }
					idx = iter.next();
					newval = ndarr.idx_int(idx) * scalar;
					newvalbyte = Utils.INT_2_BYTE(newval);
					for(int j = 0; j < 4; j++)
					{
						ndarr.modify_DATAPOOL(ndarr._idx(idx)+j, newvalbyte[j]);
					}				
				}
        		break;
        	}

        	case "NumJ.Float64":
        	{
		        double newval;
				for(int i = 0; i < ndarr.size; i++)
				{
					idx = iter.next();
					newval = ndarr.idx_double(idx) * scalar;
					newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						ndarr.modify_DATAPOOL(ndarr._idx(idx)+j, newvalbyte[j]);
					}				
				}
        		break;
        	}

        	default :
        		break;
        }
	}
	protected static void scalar(NDArray ndarr, double scalar)
	{
		// should check type
		if(!(ndarr.dtype.NAME.equals("NumJ.Int32") || ndarr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		} // newarr is a int32 array
		ArrayList<int[]> idxs = Utils.getAllIdxs(ndarr);
        Iterator<int[]> iter 
            = idxs.iterator();
        int [] idx;
        byte[] newvalbyte;

        switch(ndarr.dtype.NAME)
        {
        	case "NumJ.Int32":
        	{
        		// NDArray newarr = new NDArray();

        		byte[] new_DP = new byte[ndarr.size * 8];
		        double newval;
				for(int i = 0; i < ndarr.size; i++)
				{
					// try
					// {
					// 	idx = iter.next();	
					// }
					// catch(java.util.NoSuchElementException e)
					// {
					// 	System.out.println("inner wrong array size"); // not expect it to happen;
					// }
					idx = iter.next();
					newval = ndarr.idx_int(idx) * scalar;
					newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						new_DP[ndarr._idx(idx)+j] = newvalbyte[j];
					}				
				}
        		Float64 t = new Float64();
        		DType type = new DType(t);
        		ndarr.setter_dtype(type);
        		ndarr.setter_shape(ndarr.getter_shape());
        		ndarr.setter_DATAPOOL(new_DP);
        	}

        	case "NumJ.Float64":
        	{
        		// NDArray newarr = NDArray.deepCopy(ndarr);
		        double newval;
				for(int i = 0; i < ndarr.size; i++)
				{
					idx = iter.next();
					newval = ndarr.idx_double(idx) * scalar;
					newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						ndarr.modify_DATAPOOL(ndarr._idx(idx)+j, newvalbyte[j]);
					}				
				}
        	}

        	default :
        		break;
        }
	}
	// protected static NDArray innerProduct(NDArray ndarr1, NDArray ndarr2)
	// {
	// 	if(!(ndarr1.dtype.NAME.equals("NumJ.Int32") || ndarr1.dtype.NAME.equals("NumJ.Float64")))
	// 	{
	// 		throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
	// 	}
	// 	if(!(ndarr2.dtype.NAME.equals("NumJ.Int32") || ndarr2.dtype.NAME.equals("NumJ.Float64")))
	// 	{
	// 		throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
	// 	} // newarr is a int32 array
	// }

	/***************
	* if one of array is 1d: broadcast + multiply
	* if both are 2d	: matmul
	* if one of array is over 3d and no 1d array: dotproduct
	*/

	// util method: vector x vector
	// we only handle 1d vector
	// return a element instead of a array
	// we'll use double under the hood for all dtype
	private static double innerProduct(NDArray vec1, NDArray vec2)
	{
		if(vec1.numDims != 1 || vec2.numDims != 1)
		{
			throw new IllegalArgumentException("The input should be a 1d vector");
		}
		if(vec1.size != vec2.size)
		{
			throw new IllegalArgumentException("The input vector size should be the same");
		}

		String name1 = vec1.dtype.NAME;
		String name2 = vec2.dtype.NAME;
		if(!name1.equals("NumJ.Int32") && !name1.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("vec1 wrong type!");
		}
		if(!name2.equals("NumJ.Int32") && !name2.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("vec2 wrong type!");
		}

		double sum = 0;
		if(name1.equals("NumJ.Int32"))
		{
			if(name2.equals("NumJ.Int32"))
			{
				for(int i = 0; i<vec1.size; i++)
				{
					sum += vec1.idx_int(i) * vec2.idx_int(i);
				}		
			}
			else
			{
				for(int i = 0; i<vec1.size; i++)
				{
					sum += vec1.idx_int(i) * vec2.idx_double(i);
				}				
			}
		}
		else
		{
			if(name2.equals("NumJ.Int32"))
			{
				for(int i = 0; i<vec1.size; i++)
				{
					sum += vec1.idx_double(i) * vec2.idx_int(i);
				}		
			}
			else
			{
				for(int i = 0; i<vec1.size; i++)
				{
					sum += vec1.idx_double(i) * vec2.idx_double(i);
				}				
			}			
		}
		return sum;
	}

	// 2d x 2d
	// 2d x 1d
	// 1d x 2d
	// 1d x 1d
	protected static NDArray matmul(NDArray arr1, NDArray arr2)
	{
		int flag = 0;
		// check dimension and broadcast
		if(arr1.numDims > 2 || arr2.numDims >2)
		{
			throw new IllegalArgumentException("Only support 2d mat mul");
		}
		if(arr1.numDims == 1)
		{
			int [] broad_shape = {1, arr1.shape[0]}; // we visit here so its ok to not use getter_shape
			arr1.setter_shape(broad_shape);
			flag += 1;
		}
		if(arr2.numDims == 1)
		{
			int [] broad_shape = {arr2.shape[0], 1}; // we visit here so its ok to not use getter_shape
			arr2.setter_shape(broad_shape);
			flag += 2;
		}
		if(arr1.shape[1] != arr2.shape[0])
		{
			throw new IllegalArgumentException("can't do matmul on unaligned dimensions");
		}
		// check dtype
		String name1 = arr1.dtype.NAME;
		String name2 = arr2.dtype.NAME;
		if(!name1.equals("NumJ.Int32") && !name1.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("vec1 wrong type!");
		}
		if(!name2.equals("NumJ.Int32") && !name2.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("vec2 wrong type!");
		}	

		Float64 t = new Float64();
		DType type = new DType(t);
		NDArray newarr = new NDArray();
		newarr.setter_dtype(type);
		int[] new_shape = {arr1.shape[0], arr2.shape[1]};
		newarr.setter_shape(new_shape);
		byte[] new_DP = new byte[newarr.size * 8];
		for(int i = 0; i < arr1.shape[0]; i++)
		{
			for(int j = 0; j < arr2.shape[1]; j++)
			{
				int[] index = {i, j};
				double val = innerProduct(Matrix.getRow(arr1, i), Matrix.getCol(arr2, j));
				byte[] val_byte = Utils.DOUBLE_2_BYTE(val);
				for (int k = 0; k < 8; k++) {
					new_DP[newarr._idx(index) + k] = val_byte[k];
				}
			}
		}
		newarr.setter_DATAPOOL(new_DP);

		// clean the dimension
		switch(flag)
		{
			case 1:
			{
				arr1.setter_shape(Arrays.copyOfRange(arr1.shape, 1, arr1.shape.length));
				newarr.setter_shape(Arrays.copyOfRange(newarr.shape, 1, newarr.shape.length));
				break;
			}
			case 2:
			{
				arr2.setter_shape(Arrays.copyOfRange(arr2.shape, 0, arr2.shape.length-1));
				newarr.setter_shape(Arrays.copyOfRange(newarr.shape, 0, newarr.shape.length-1));
				break;
			}
			case 3:
			{
				arr1.setter_shape(Arrays.copyOfRange(arr1.shape, 1, arr1.shape.length));
				arr2.setter_shape(Arrays.copyOfRange(arr2.shape, 0, arr2.shape.length-1));
				newarr.setter_shape(Arrays.copyOfRange(newarr.shape, 1, newarr.shape.length-1));
				break;
			}
			default:
			{
				break;
			}
		}
		return newarr;
	}

	// store in arr1
	// all converted to double in the end
	protected static void multiply(NDArray arr1, NDArray arr2) // element-wise multiplication
	{
		String name1 = arr1.dtype.NAME;
		String name2 = arr2.dtype.NAME;
		if(!name1.equals("NumJ.Int32") && !name1.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("arr1 wrong type!");
		}
		if(!name2.equals("NumJ.Int32") && !name2.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("arr2 wrong type!");
		}
		if(name1.equals("NumJ.Int32"))
		{
			if(name2.equals("NumJ.Int32"))
			{
				MyPairFunc mul = (bytes1, bytes2) ->{
					double val_1 = DType.parseByteInt(bytes1, 0);
					double val_2 = DType.parseByteInt(bytes2, 0);
					return DType.toByteAuto(val_1*val_2);
				};
				Function.each_pair_do(mul, arr1, arr2);
			}
			else
			{
				MyPairFunc mul = (bytes1, bytes2) ->{
					double val_1 = DType.parseByteInt(bytes1, 0);
					double val_2 = DType.parseByteDouble(bytes2, 0);
					return DType.toByteAuto(val_1*val_2);
				};
				Function.each_pair_do(mul, arr1, arr2);			
			}
		}
		else
		{
			if(name2.equals("NumJ.Int32"))
			{
				MyPairFunc mul = (bytes1, bytes2) ->{
					double val_1 = DType.parseByteDouble(bytes1, 0);
					double val_2 = DType.parseByteInt(bytes2, 0);
					return DType.toByteAuto(val_1*val_2);
				};
				Function.each_pair_do(mul, arr1, arr2);	
			}
			else
			{
				MyPairFunc mul = (bytes1, bytes2) ->{
					double val_1 = DType.parseByteDouble(bytes1, 0);
					double val_2 = DType.parseByteDouble(bytes2, 0);
					return DType.toByteAuto(val_1*val_2);
				};
				Function.each_pair_do(mul, arr1, arr2);	
			}			
		}

	}

	// only support <=2d array with the same shape
	// we don't do broadcast here
	// 
	public static NDArray matadd(NDArray ndarr1, NDArray ndarr2)
	{
		// check dtype
		String name1 = ndarr1.dtype.NAME;
		String name2 = ndarr2.dtype.NAME;
		if(!name1.equals("NumJ.Int32") && !name1.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("arr1 wrong type!");
		}
		if(!name2.equals("NumJ.Int32") && !name2.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("arr2 wrong type!");
		}
		// check dimension
		// if(!Arrays.equals(arr1.shape, arr2.shape))
		// {
		// 	throw new IllegalArgumentException("Only support addtion with the same dimension");
		// }
		NDArray arr1 = NDArray.deepCopy(ndarr1);
		NDArray arr2 = NDArray.deepCopy(ndarr2);
		NDArray[] a = Matrix.broadcast(arr1, arr2);
		arr1 = a[0];
		arr2 = a[1];
		Float64 F = new Float64();
		Int32 I = new Int32();
		// NDArray newarr = new NDArray();

		// check broadcast
		// if(arr1.numDims == 1 && arr2.numDims == 1)
		// {
		// 			return arr1.DATA_POOL[]
		// }
		// if(arr.numDims == 2 && arr2.numDims == 2)
		// {

		// }
		// if(arr.numDims == 2 && arr2.numDims == 1)
		// {
			
		// }
		// if(arr.numDims == 1 && arr2.numDims == 2)
		// {
			
		// }
		if(name1.equals("NumJ.Int32"))
		{
			if(name2.equals("NumJ.Int32"))
			{
				// DType t = new DType(I);
				// newarr.setter_dtype(t);
				// byte[] new_dp = new byte[arr1.size *4];
				// newarr.setter_shape(arr1.getter_shape());
				// newarr.setter_DATAPOOL(new_dp);
				for(int i = 0; i<arr1.size; i++)
				{
					int newval = arr1.dtype.parseByteInt(arr1.DATA_POOL, i*4) + arr2.dtype.parseByteInt(arr2.DATA_POOL, i*4);
					byte[] newvalbyte = Utils.INT_2_BYTE(newval);
					for(int j = 0; j < 4; j++)
					{
						arr1.modify_DATAPOOL(i*4+j, newvalbyte[j]);
					}				
				}		
			}
			else
			{
				byte[] new_dp = new byte[arr1.size * 8];
				for(int i = 0; i<arr1.size; i++)
				{
					double newval = arr1.dtype.parseByteInt(arr1.DATA_POOL, i*4) + arr2.dtype.parseByteDouble(arr2.DATA_POOL, i*8);
					byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						new_dp[i*8+j] = newvalbyte[j] ;
					}				
				}
				DType t = new DType(F);
				arr1.setter_dtype(t);
				arr1.setter_shape(arr1.getter_shape());
				arr1.setter_DATAPOOL(new_dp);				
			}
		}
		else
		{
			if(name2.equals("NumJ.Int32"))
			{
				// DType t = new DType(F);
				// newarr.setter_dtype(t);
				// byte[] new_dp = new byte[arr1.size *8];
				// newarr.setter_shape(arr1.getter_shape());
				// newarr.setter_DATAPOOL(new_dp);
				for(int i = 0; i<arr1.size; i++)
				{
					double newval = arr1.dtype.parseByteDouble(arr1.DATA_POOL, i*8) + arr2.dtype.parseByteInt(arr2.DATA_POOL, i*4);
					byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						arr1.modify_DATAPOOL(i*8+j, newvalbyte[j]);
					}				
				}		
			}
			else
			{
				// DType t = new DType(F);
				// newarr.setter_dtype(t);
				// byte[] new_dp = new byte[arr1.size *8];
				// newarr.setter_shape(arr1.getter_shape());
				// newarr.setter_DATAPOOL(new_dp);
				for(int i = 0; i<arr1.size; i++)
				{
					double newval = arr1.dtype.parseByteDouble(arr1.DATA_POOL, i*8) + arr2.dtype.parseByteDouble(arr2.DATA_POOL, i*8);
					byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						arr1.modify_DATAPOOL(i*8+j, newvalbyte[j]);
					}				
				}		
			}			
		}
		return arr1;
	}

	protected static void add_scalar(NDArray ndarr, int scalar)
	{
		// should check type
		if(!(ndarr.dtype.NAME.equals("NumJ.Int32") || ndarr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		}
		// NDArray newarr = NDArray.deepCopy(ndarr);

        switch(ndarr.dtype.NAME)
        {
        	case "NumJ.Int32":
        	{
		        int newval;
				for(int i = 0; i < ndarr.size; i++)
				{
					newval = ndarr.dtype.parseByteInt(ndarr.DATA_POOL, i*4) + scalar;
					byte[] newvalbyte = Utils.INT_2_BYTE(newval);
					for(int j = 0; j < 4; j++)
					{
						ndarr.modify_DATAPOOL(i*4+j, newvalbyte[j]);
					}				
				}
        		break;
        	}

        	case "NumJ.Float64":
        	{
        		byte[] new_dp = new byte[ndarr.size * 8];
		        double newval;
				for(int i = 0; i < ndarr.size; i++)
				{
					newval = ndarr.dtype.parseByteDouble(ndarr.DATA_POOL, i*8) + scalar;
					byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						new_dp[i*8+j] = newvalbyte[j];
					}				
				}
        		Float64 f = new Float64();
        		DType t = new DType(f);
        		ndarr.setter_dtype(t);
        		ndarr.setter_shape(ndarr.shape);
				ndarr.setter_DATAPOOL(new_dp);

        		break;
        	}

        	default :
        		break;
        }
   	}
	protected static void add_scalar(NDArray ndarr, double scalar)
	{
		// should check type
		if(!(ndarr.dtype.NAME.equals("NumJ.Int32") || ndarr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		}
		// NDArray newarr = NDArray.deepCopy(ndarr);

		double newval;
        switch(ndarr.dtype.NAME)
        {
        	case "NumJ.Int32":
        	{
		        
				for(int i = 0; i < ndarr.size; i++)
				{
					byte[] new_dp = new byte[ndarr.size * 8];
					newval = ndarr.dtype.parseByteInt(ndarr.DATA_POOL, i*4) + scalar;
					byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						new_dp[i*4+j] = newvalbyte[j];
					}
			   		Float64 f = new Float64();
					DType t = new DType(f);
					ndarr.setter_dtype(t);
					ndarr.setter_shape(ndarr.shape);
					ndarr.setter_DATAPOOL(new_dp);				
				}
        		break;
        	}

        	case "NumJ.Float64":
        	{
				for(int i = 0; i < ndarr.size; i++)
				{
					newval = ndarr.dtype.parseByteDouble(ndarr.DATA_POOL, i*8) + scalar;
					byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						ndarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
					}				
				}
        		break;
        	}

        	default :
        		break;
        }			
	}

	protected static void minus(NDArray ndarr)
	{
		scalar(ndarr, -1);
		// // should check type
		// if(!(ndarr.dtype.NAME.equals("NumJ.Int32") || ndarr.dtype.NAME.equals("NumJ.Float64")))
		// {
		// 	throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		// }

		// // NDArray newarr = NDArray.deepCopy(ndarr);
		// if(ndarr.dtype.NAME == "NumJ.Int32")
		// {
		// 	int newval;
		// 	for(int i = 0; i < ndarr.size; i++)
		// 	{
		// 		newval = -ndarr.dtype.parseByteInt(ndarr.DATA_POOL, i*4);
		// 		byte[] newvalbyte = Utils.INT_2_BYTE(newval);
		// 		for(int j = 0; j < 4; j++)
		// 		{
		// 			ndarr.modify_DATAPOOL(i*4+j, newvalbyte[j]);
		// 		}				
		// 	}
		// }
		// else
		// {
		// 	double newval;
		// 	for(int i = 0; i < ndarr.size; i++)
		// 	{
		// 		newval = -ndarr.dtype.parseByteDouble(ndarr.DATA_POOL, i*8);
		// 		byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
		// 		for(int j = 0; j < 8; j++)
		// 		{
		// 			ndarr.modify_DATAPOOL(i*8+j, newvalbyte[j]);
		// 		}				
		// 	}
		// }
	}


	// in-place
	protected static void reciprocal(NDArray ndarr)
	{
		// should check type
		if(!(ndarr.dtype.NAME.equals("NumJ.Int32") || ndarr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		}

		// NDArray newarr = NDArray.deepCopy(ndarr);
		byte[] new_dp = new byte[ndarr.size * 8];
		double newval;
		if(ndarr.dtype.NAME == "NumJ.Int32")
		{
			for(int i = 0; i < ndarr.size; i++)
			{
				newval = 1f/ndarr.dtype.parseByteInt(ndarr.DATA_POOL, i*4);
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					new_dp[i*8+j] = newvalbyte[j];
				}
			}
		}
		else
		{
			for(int i = 0; i < ndarr.size; i++)
			{
				newval = 1/ndarr.dtype.parseByteDouble(ndarr.DATA_POOL, i*8);
				byte[] newvalbyte = Utils.DOUBLE_2_BYTE(newval);
				for(int j = 0; j < 8; j++)
				{
					new_dp[i*8+j] = newvalbyte[j];
				}				
			}
		}
		Float64 f = new Float64();
		DType t = new DType(f);
		ndarr.setter_dtype(t);
		ndarr.setter_shape(ndarr.getter_shape());
		ndarr.setter_DATAPOOL(new_dp);
	}

	// return 1d array!!
	protected static NDArray getRow(NDArray ndarr, int row)
	{

		if(ndarr.numDims != 2)
		{
			throw new IllegalArgumentException("array not 2d!");
		}
		int [] shape = ndarr.getter_shape();
		int rowNum = shape[0]; // 1st shape of 2d array is the row number
		int colNum = shape[1];
		if(row >= rowNum)
		{
			throw new IllegalArgumentException("row out of boundary");
		}
		NDArray rowarr = new NDArray();
		rowarr.setter_dtype(ndarr.getter_dtype());
		int[] new_shape = {colNum};
		rowarr.setter_shape(new_shape);
		int itemsize = ndarr.getter_dtype().itemsize;
		byte[] new_DP = new byte[colNum * itemsize];
		rowarr.setter_DATAPOOL(new_DP);
		// get all indexs
		for(int i = 0; i < colNum*itemsize; i++)
		{
			rowarr.modify_DATAPOOL(i, ndarr.get_DP_at(ndarr._idx(row, i/itemsize) + i%itemsize));
		}
		return rowarr;
	}
	protected static NDArray getCol(NDArray ndarr, int col) 
	{
		if(ndarr.numDims != 2)
		{
			throw new IllegalArgumentException("array not 2d!");
		}
		int[] shape = ndarr.getter_shape();
		int rowNum = shape[0]; // 1st shape of 2d array is the row number
		int colNum = shape[1];
		if(col >= colNum)
		{
			throw new IllegalArgumentException("row out of boundary");
		}
		NDArray rowarr = new NDArray();
		rowarr.setter_dtype(ndarr.getter_dtype());
		int[] new_shape = {rowNum};
		rowarr.setter_shape(new_shape);
		int itemsize = ndarr.getter_dtype().itemsize;
		byte[] new_DP = new byte[rowNum * itemsize];
		rowarr.setter_DATAPOOL(new_DP);
		// get all indexs
		for(int i = 0; i < rowNum*itemsize; i++)
		{
			rowarr.modify_DATAPOOL(i, ndarr.get_DP_at(ndarr._idx(i/itemsize, col) + i%itemsize));
		}
		return rowarr;		
	}

	// in-place ops!!
	// involving reshape
	public static NDArray[] broadcast(NDArray arr1, NDArray arr2)
	{
		int l1 = arr1.shape.length;
		int l2 = arr2.shape.length;
		if(l1 >= l2)
		{
			int[] new_shape = new int[l1];
			int[] target = new int[l1];
			for(int i = 0; i < l1-l2; i++)
			{
				new_shape[i] = 1;
				target[i] = arr1.shape[i];
			}
			for(int i = l1-l2; i < l1; i++)
			{
				new_shape[i] = arr2.shape[i-l1+l2];
				target[i] = Math.max(arr1.shape[i], arr2.shape[i-l1+l2]);
			}
			arr2.reshape(new_shape);
			arr1 = one_way_cast(arr1, target);
			arr2 = one_way_cast(arr2, target);
		}
		if(l1 < l2)
		{
			int[] new_shape = new int[l2];
			int[] target = new int[l2];
			for(int i = 0; i < l2-l1; i++)
			{
				new_shape[i] = 1;
				target[i] = arr2.shape[i];
			}
			for(int i = l2-l1; i < l2; i++)
			{
				new_shape[i] = arr1.shape[i+l1-l2];
				target[i] = Math.max(arr1.shape[i+l1-l2], arr2.shape[i]);
			}
			arr1.reshape(new_shape);
			arr1 = one_way_cast(arr1, target);
			arr2 = one_way_cast(arr2, target);
		}
		NDArray[] result ={arr1, arr2};
		return result;
	}

	// wont' change original
	private static NDArray one_way_cast(NDArray arr, int[] target)
	{
		if(arr.shape.length != target.length)
		{
			throw new IllegalArgumentException("can't finish the casting");
		}
		NDArray result = NDArray.deepCopy(arr);
		for(int i = target.length - 1; i >= 0; i--)
		{
			int s1 = arr.shape[i];
			int s2 = target[i];
			if(s1 != s2)
			{
				if(s1 == 1)
				{
					result = stretch_dimension(result, i, s2);
				}
				else if(s2 == 1)
				{
					result = stretch_dimension(result, i, s1);
				}
				else
				{
					throw new IllegalArgumentException("can't cast");
				}
			}
		}
		return result;
	}

	// wont change the original
	private static NDArray stretch_dimension(NDArray arr, int dim, int to)
	{
		if(dim >= arr.shape.length || to < 1)
		{
			throw new IllegalArgumentException("Illegal args");
		}
		if(arr.shape[dim] != 1)
		{
			throw new IllegalArgumentException("Cant stretch, not 1d");
		}
		
		int [] newshape = Utils.deepCopyIntArray(arr.shape);
		newshape[dim] = to;
		// System.out.println(Arrays.toString(newshape));
		NDArray new_arr = NDArray.zeros(newshape, arr.dtype, null);
		ArrayList<int[]> old_idxs = Utils.getAllIdxs(arr);
        for(int[] idx : old_idxs)
        {
        	byte[] old_bytes = arr._idx_byte(idx);
        	for(int i = 0; i < to; i++)
        	{
        		// int[] new_idx = Utils.insertArray(idx, dim, i);
        		int [] new_idx = Utils.deepCopyIntArray(idx);
        		new_idx[dim] = i;
        		for(int j = 0; j < arr.dtype.itemsize; j++)
        		{
        			new_arr.modify_DATAPOOL(new_arr._idx(new_idx) + j, old_bytes[j]);
        		}
        	}       	
        }
        return new_arr;
	}


	public static void main(String[] args)
	{
		// float i = 1.0f;
		// System.out.println(i);
	    Float[][][] arr3d = {
			{
				{1.0f, -2.0f, 3.0f}, 
				{2.1f, 3.5f, 4.0f}
			}, 
			{ 
				{-4.5f, -5.6f, 6.5f}, 
				{2.0f, 3.7f, 1.3f}
			}
		}; 
	    Integer[][] arr2d = {
			{1, -2, 3}, 
			{-4, -5, 6}, 
		};
		Double[] arr1d={1.0,2.0,3.0,55.0,100.0,2000.0};
		Double[] arr_bc = {2.3, 5.5, 89.0};

		int[] dims1 = {2,3};
		int[] dims2 = {3,1,2,1};
		int[] dims3 = {2,6};
		int[] dims_bc = {1,3};
		NDArray ndarr1 = new NDArray(arr1d, dims1, 'C');
		NDArray ndarr2 = new NDArray(arr2d, dims2, 'C');
		NDArray ndarr3 = new NDArray(arr3d, dims3, 'C');
		NDArray ndarr_bc = new NDArray(arr_bc, dims_bc, 'C');
		// Matrix.multiply(ndarr1, ndarr2);
		// ndarr1.repr();
		// NDArray ndarr3 = new NDArray(arr3d, dims3, 'C');
		// NDArray nda = scalar(ndarr2, 3.0f);
		// Matrix.T(ndarr1).repr();
		// ndarr2.repr();
		// nda.repr();
		// NDArray row1 = Matrix.getRow(ndarr1, 0);
		// NDArray row2 = Matrix.getRow(ndarr1, 1);
		// row1.repr();
		// row2.repr();
		// NDArray col1 = Matrix.getCol(ndarr1, 0);
		// NDArray col2 = Matrix.getCol(ndarr1, 2);
		// col1.repr();
		// col2.repr();
		// System.out.println(Matrix.innerProduct(col1, col2));

		// NDArray mul = Matrix.matmul(ndarr3, ndarr1);
		// ndarr1.repr();
		// ndarr3.repr();
		// mul.repr(true);
		// NDArray tst1 = Matrix.add_scalar(ndarr2, 1);
		// NDArray tst2 = Matrix.add_scalar(mul, 1.0);
		// tst1.repr(true);
		// tst2.repr(true);

		// Matrix.reciprocal(ndarr1);
		// ndarr1.repr(true);
		// Matrix.add_scalar(ndarr1, -3);
		// ndarr1.repr();

		// Matrix.stretch_dimension(ndarr1, 1, 3).repr();
		// int[] target = {3,3,2,2};
		// Matrix.one_way_cast(ndarr2, target).repr(true);
		// NDArray[] a = Matrix.broadcast(ndarr_bc, ndarr1);
		// a[0].repr(true);
		// a[1].repr(true);
		// ndarr_bc.repr(true);
		// ndarr1.repr(true);
		// Matrix.matadd(ndarr_bc, ndarr1);
		// ndarr_bc.repr(true);
		// ndarr1.repr(true);

		// ndarr3.repr(true);
		// Matrix.matsum(ndarr3, 0, true).repr(true);
		// Matrix.matsum(ndarr3, 1, false).repr(true);

		ndarr1.repr();
		ndarr_bc.repr();
		Matrix.vstack(ndarr1, ndarr_bc).repr();
	}
}