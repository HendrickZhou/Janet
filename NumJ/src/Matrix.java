/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
// package org.NumJ.core;
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
        		newarr.setter_shape(ndarr.getter_shape());
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
		if(name1 != "NumJ.Int32" && name1 != "NumJ.Float64")
		{
			throw new IllegalArgumentException("vec1 wrong type!");
		}
		if(name2 != "NumJ.Int32" && name2 != "NumJ.Float64")
		{
			throw new IllegalArgumentException("vec2 wrong type!");
		}

		double sum = 0;
		if(name1 == "NumJ.Int32")
		{
			if(name2 == "NumJ.Int32")
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
			if(name2 == "NumJ.Int32")
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
		if(name1 != "NumJ.Int32" && name1 != "NumJ.Float64")
		{
			throw new IllegalArgumentException("vec1 wrong type!");
		}
		if(name2 != "NumJ.Int32" && name2 != "NumJ.Float64")
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


	// protected static NDArray multiply(); // element-wise multiplication


	// protected static void broadcast(NDArray arr, NDArray result)
	// {

	// }

	// public static NDArray add_broadcast(NDArray arr1, NDArray arr2)
	// {
	// 	// check dimension and broadcast
	// 	if(arr1.numDims == 1)
	// 	{
	// 		int [] broad_shape = {1, arr1.shape[0]}; // we visit here so its ok to not use getter_shape
	// 		arr1.setter_shape(broad_shape);
	// 	}
	// 	if(arr2.numDims == 1)
	// 	{
	// 		int [] broad_shape = {arr2.shape[0], 1}; // we visit here so its ok to not use getter_shape
	// 		arr2.setter_shape(broad_shape);
	// 	}
	// 	if(arr1.shape[1] != arr2.shape[0])
	// 	{
	// 		throw new IllegalArgumentException("can't do matmul on unaligned dimensions");
	// 	}
	// 	// check dtype
	// 	String name1 = arr1.dtype.NAME;
	// 	String name2 = arr2.dtype.NAME;
	// 	if(name1 != "NumJ.Int32" && name1 != "NumJ.Float64")
	// 	{
	// 		throw new IllegalArgumentException("vec1 wrong type!");
	// 	}
	// 	if(name2 != "NumJ.Int32" && name2 != "NumJ.Float64")
	// 	{
	// 		throw new IllegalArgumentException("vec2 wrong type!");
	// 	}			
	// }

	// return 1d array!!
	public static NDArray getRow(NDArray ndarr, int row)
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
	public static NDArray getCol(NDArray ndarr, int col) 
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
		Float[] arr1d={1f,2f,3f,55f,100f,2000f};

		int[] dims1 = {6};
		int[] dims2 = {3,2};
		int[] dims3 = {2,6};
		NDArray ndarr1 = new NDArray(arr1d, dims1, 'C');
		NDArray ndarr2 = new NDArray(arr2d, dims2, 'C');
		NDArray ndarr3 = new NDArray(arr3d, dims3, 'C');
		NDArray nda = scalar(ndarr2, 3.0f);
		ndarr1.repr();
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

		NDArray mul = Matrix.matmul(ndarr3, ndarr1);
		ndarr1.repr();
		ndarr3.repr();
		mul.repr(true);
	}
}