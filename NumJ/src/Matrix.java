/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
// package org.NumJ.core;
import java.util.ArrayList;
import java.util.Iterator;

/**
* This class only handle matrix operation with 2 or lower dimensions.
*
*
*/
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
						newarr.modify_DATAPOOL(newarr._idx(idx)+j, newvalbyte[j]);
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
						newarr.modify_DATAPOOL(newarr._idx(idx)+j, newvalbyte[j]);
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
        		NDArray newarr = new NDArray();
        		Float64 t = new Float64();
        		DType type = new DType(t);
        		newarr.setter_dtype(type);
        		newarr.setter_shape(ndarr.shape);
        		byte[] new_DP = new byte[ndarr.size * 8];
        		newarr.setter_DATAPOOL(new_DP);

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
						newarr.modify_DATAPOOL(newarr._idx(idx)+j, newvalbyte[j]);
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
						newarr.modify_DATAPOOL(newarr._idx(idx)+j, newvalbyte[j]);
					}				
				}
        		return newarr;
        	}

        	default :
        		NDArray newarr = new NDArray();
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



	public static NDArray getRow(NDArray ndarr, int row)
	{
		if(ndarr.numDims != 2)
		{
			throw new IllegalArgumentException("array not 2d!");
		}
		int rowNum = ndarr.shape[0]; // 1st shape of 2d array is the row number
		int colNum = ndarr.shape[1];
		NDArray rowarr = new NDArray();
		rowarr.setter_dtype(ndarr.gett)
   		Float64 t = new Float64();
		DType type = new DType(t);
		byte[] new_DP = new byte[ndarr.size * 8];
		int[] new_s_k = new int[ndarr.numDims];
		int[] new_shape = new int[ndarr.numDims];
		NDArray.calSisParams(new_s_k,  new_shape, 8, ndarr.shape, ndarr.order);
		NDArray rowarr = new NDArray(new_shape, type, ndarr.size, ndarr.numDims, new_DP, new_s_k, ndarr.order);

		// get all indexs
		for(i = 0; i < colNum; i++)
		{

		}
	}
	// public static NDArray getCol(NDArray ndarr, int col) 

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
		Integer[] arr1d={1,2,3,55,100,2000};

		int[] dims1 = {2,3,1};
		int[] dims2 = {2,3,1};
		int[] dims3 = {2,3,2};
		NDArray ndarr1 = new NDArray(arr1d, dims1, 'C');
		NDArray ndarr2 = new NDArray(arr2d, dims2, 'C');
		NDArray ndarr3 = new NDArray(arr3d, dims3, 'C');
		NDArray nda = scalar(ndarr2, 3.0f);
		ndarr2.repr();
		nda.repr();
	}
}