/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
// package org.NumJ.core;
import java.util.ArrayList;
import java.util.Iterator;

class Matrix
{
	// we do not handle uncertain dimension array here
	// Since the type conversion haven't been finished yet, all the operation are assumed
	// to be the same data type
	// but since we're using the java's default number operation type casting, we'll not check the 
	// type correctness here as this part actually has some flexibility in type
	//
	// ndarr can only be int type or double type
	protected static NDArray scalar(NDArray ndarr, int scalar)
	{
		// should check type
		if(!(ndarr.dtype.NAME.equals("NumJ.Int32") || ndarr.dtype.NAME.equals("NumJ.Float64")))
		{
			throw new IllegalArgumentException("conver the array to standard(Int32/Float64) first");
		}
		NDArray newarr = NDArray.deepCopy(ndarr); // newarr is a int32 array
		ArrayList<int[]> idxs = Utils.getAllIdxs(newarr);
        Iterator<int[]> iter 
            = idxs.iterator();
        int [] idx;
        byte[] newvalbyte;

        switch(ndarr.dtype.NAME)
        {
        	case "NumJ.Int32":
        	{
		        int newval;
				for(int i = 0; i < newarr.size; i++)
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
					newval = newarr.idx_int(idx) * scalar;
					newvalbyte = Utils.INT_2_BYTE(newval);
					for(int j = 0; j < 4; j++)
					{
						newarr.DATA_POOL[newarr._idx(idx)+j] = newvalbyte[j];
					}				
				}
        		break;
        	}

        	case "NumJ.Float64":
        	{
		        double newval;
				for(int i = 0; i < newarr.size; i++)
				{
					idx = iter.next();
					newval = newarr.idx_double(idx) * scalar;
					newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						newarr.DATA_POOL[newarr._idx(idx)+j] = newvalbyte[j];
					}				
				}
        		break;
        	}

        	default :
        		break;
        }

		return newarr;
	}
	protected static NDArray scalar(NDArray ndarr, double scalar)
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
        		Float64 t = new Float64();
        		DType type = new DType(t);
        		byte[] new_DP = new byte[ndarr.size * 8];
        		int[] new_s_k = new int[ndarr.numDims];
        		int[] new_shape = new int[ndarr.numDims];
        		NDArray.calSisParams(new_s_k,  new_shape, 8, ndarr.shape, ndarr.order);
        		NDArray newarr = new NDArray(new_shape, type, ndarr.size, ndarr.numDims, new_DP, new_s_k, ndarr.order);
		        double newval;
				for(int i = 0; i < newarr.size; i++)
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
						newarr.DATA_POOL[newarr._idx(idx)+j] = newvalbyte[j];
					}				
				}
				return  newarr;
        	}

        	case "NumJ.Float64":
        	{
        		NDArray newarr = NDArray.deepCopy(ndarr);
		        double newval;
				for(int i = 0; i < newarr.size; i++)
				{
					idx = iter.next();
					newval = newarr.idx_double(idx) * scalar;
					newvalbyte = Utils.DOUBLE_2_BYTE(newval);
					for(int j = 0; j < 8; j++)
					{
						newarr.DATA_POOL[newarr._idx(idx)+j] = newvalbyte[j];
					}				
				}
        		return newarr;
        	}

        	default :
        		NDArray newarr = new NDArray(0,0);
        		return newarr;
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
	// protected static NDArray dotProduct();
	// protected static NDArray mmul();
	public  static void main(String[] args)
	{
	    Integer[][][] arr3d = {
			{
				{1, -2, 3}, 
				{2, 3, 4}
			}, 
			{ 
				{-4, -5, 6}, 
				{2, 3, 1}
			}
		}; 
	    Integer[][] arr2d = {
			{1, -2, 3}, 
			{-4, -5, 6}, 
		};
		Integer[] arr1d={1,2,3,55,100,2000};

		int[] dims1 = {2,3,1};
		int[] dims2 = {2,3,1};
		int[] dims3 = {2,3,2};
		NDArray ndarr1 = new NDArray(arr1d, dims1, 'C');
		NDArray ndarr2 = new NDArray(arr2d, dims2, 'C');
		NDArray ndarr3 = new NDArray(arr3d, dims3, 'C');
		NDArray nda = scalar(ndarr1, 3.0);
		ndarr1.repr();
		nda.repr();
	}
}