/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
// package org.NumJ.core;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
/**
*	This data structure is meant be flexiable for matrix operation
*/
public class NDArray
{
	// fields
    public int[] shape; //d_k
    public DType dtype;
    public int size;
    public int numDims;

    protected byte[] DATA_POOL; // data storage
    private int[] s_k;
    private int order = 0; // 0 for 'C', 1 for 'F'    


	// Random Tools
    private static Random random;
    private static long seed;
    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }
    public static void setSeed(long s) {
        seed = s;
        random = new Random(seed);
    }
    public static long getSeed() {
        return seed;
    }




	// Constructor
	public NDArray(int size, int numDims)
	{
		this.numDims = numDims;
		this.size = size;
		this.s_k = new int[numDims];
		this.shape = new int[numDims];
		this.DATA_POOL = new byte[size];
		this.dtype = new DType();
	}
	public NDArray(int[] shape, DType dtype, int size, int numDims, byte[] DATA_POOL, int[] s_k, int order)
	{
		this.numDims = numDims;
		this.size = size;
		this.s_k = s_k;
		this.shape = shape;
		this.DATA_POOL = DATA_POOL;
		this.dtype = dtype;
		this.order = order;
	}
	/**
	* Randomly generated
	* By default, int/float will be between 0 and 100
	* @param dtype 	data type of this NDArray, only support java non-primitive type
	*/
	public NDArray(int[] dims, DType dtype, Character order)
	{
		order = order != null ? order : 'C';
		this.order = order.equals('C') ? 0 : 1;
		// safty check

		this.dtype = dtype;
		this.numDims = dims.length;
		int size = 1;
		for(int d = 0; d < this.numDims; d++)
		{
			size *= dims[d];
		}
		this.size = size;
		this.shape = new int[this.numDims];
		this.s_k = new int[this.numDims];
		calSisParams(this.s_k, this.shape, this.dtype.itemsize, dims, this.order);


		ArrayList<byte[]> result = new ArrayList<byte[]>();
		switch(this.dtype.NAME)
		{
			case "NumJ.Int32":	
			{
				for(int x = 0; x < this.size; x++)
				{
					byte[] next = this.dtype.toByte(random.nextInt(10000));
					result.add(next);
				}
				break;
			}
			case "NumJ.Int16":
			{
				for(int x = 0; x < this.size; x++)
				{
					byte[] next = this.dtype.toByte((short)random.nextInt(10000));
					result.add(next);
				}
				break;
			}
			case "NumJ.Int64":
			{
				for(int x = 0; x < this.size; x++)
				{
					byte[] next = this.dtype.toByte(random.nextLong());
					result.add(next);
				}
				break;
			}
			case "NumJ.Float32":
			{
				for(int x = 0; x < this.size; x++)
				{
					byte[] next = this.dtype.toByte(random.nextFloat());
					result.add(next);
				}
				break;
			}
			case "NumJ.Float64":
			{
				for(int x = 0; x < this.size; x++)
				{
					byte[] next = this.dtype.toByte(random.nextDouble());
					result.add(next);
				}
				break;
			}
			default:
				throw new IllegalArgumentException("bad input Array type"); // this line is not likely to be checked
		}
		this.DATA_POOL = Utils.concatBytesArr(result);		
	}

	/**
	* Only support Array with regular dimensions, if not will throw the exception
	* Only support Simple Array with fixed dimension 1d, 2d & 3d
	* Only receive primitive java type: short, int, long, float, double
	* @param array 	Arrays with regular dimensions, consist of Wrapper objects
	* @param order 	order of layout, 'C'(C-style) or 'F'(Fortran style)	
	*/
	// public NDArray(Object arr, int [] dims, Character order)
	// {
	// 	CLass<?> arrClass = arr.getClass();
	// 	if(!arrClass.isArray())
	// 	{
	// 		throw new IllegalArgumentException("non java array");
	// 	}
	// 	switch(arrClass)
	// 	{
	// 		case Integer.class:
	// 		{

	// 		}

	// 		case Short.class:
	// 		{

	// 		}

	// 		case Long.class:
	// 		{

	// 		}
	// 	}
	// }

	// 1d
	public <N extends Number> NDArray(N[] array, int[] dims, Character order)
	{
		order = order != null ? order : 'C';
		this.order = order.equals('C') ? 0 : 1;
		// safty check
		//	- if input array  is reasonable
		// 	- if dims are right
		//	- if shape is reasonable 
		// 	- if order is in right format

		this.numDims = dims.length;
		int size = 1;
		for(int d = 0; d < this.numDims; d++)
		{
			size *= dims[d];
		}
		this.size = size;
		this.shape = new int[this.numDims];
		String typeName = array[0].getClass().getSimpleName();
		switch(typeName)
		{
			case "Short": { }
			case "Integer": { }
			case "Long":
			{
				Int64 t = new Int64();
				this.dtype = new DType(t);
				break;
			}
			case "Float": { }
			case "Double":
			{
				Float64 t = new Float64();
				this.dtype = new DType(t);
				break;
			}
			default:
				throw new IllegalArgumentException("bad input Array type"); // this line is not likely to be checked
		}
		this.s_k = new int[this.numDims];
		calSisParams(this.s_k, this.shape, this.dtype.itemsize, dims, this.order);

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		if(this.dtype.NAME.equals("NumJ.Int64"))
		{
			for(int x = 0; x < array.length; x++)
			{
				byte[] next = this.dtype.toByte(array[x].longValue());
				result.add(next);
			}
		}
		else
		{
			for(int x = 0; x < array.length; x++)
			{
				byte[] next = this.dtype.toByte(array[x].doubleValue());
				result.add(next);
			}
		}

		this.DATA_POOL = Utils.concatBytesArr(result);
	}
	// 2d
	public <N extends Number> NDArray(N[][] array, int[] dims, Character order)
	{
		order = order != null ? order : 'C';
		this.order = order.equals('C') ? 0 : 1;
		// safty check
		//	- if input array  is reasonable
		// 	- if dims are right
		//	- if shape is reasonable 
		// 	- if order is in right format

		this.numDims = dims.length;
		int size = 1;
		for(int d = 0; d < this.numDims; d++)
		{
			size *= dims[d];
		}
		this.size = size;
		this.shape = new int[this.numDims];
		String typeName = array[0][0].getClass().getSimpleName();
		switch(typeName)
		{
			case "Short": { }
			case "Integer": { }
			case "Long":
			{
				Int64 t = new Int64();
				this.dtype = new DType(t);
				break;
			}
			case "Float": { }
			case "Double":
			{
				Float64 t = new Float64();
				this.dtype = new DType(t);
				break;
			}
			default:
				throw new IllegalArgumentException("bad input Array type"); // this line is not likely to be checked
		}
		this.s_k = new int[this.numDims];
		calSisParams(this.s_k, this.shape, this.dtype.itemsize, dims, this.order);

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		if(this.dtype.NAME.equals("NumJ.Int64"))
		{
			for(int x = 0; x < array.length; x++)
			{
				for(int y = 0; y < array[0].length; y++)
				{
					byte[] next = this.dtype.toByte(array[x][y].longValue());
					result.add(next);
				}
			}
		}
		else
		{
			for(int x = 0; x < array.length; x++)
			{
				for(int y = 0; y < array[0].length; y++)
				{
					byte[] next = this.dtype.toByte(array[x][y].doubleValue());
					result.add(next);
				}
			}
		}

		this.DATA_POOL = Utils.concatBytesArr(result);
	}

	// 3d
	public <N extends Number> NDArray(N[][][] array, int[] dims, Character order)
	{
		order = order != null ? order : 'C';
		this.order = order.equals('C') ? 0 : 1;
		// safty check
		//	- if input array  is reasonable
		// 	- if dims are right
		//	- if shape is reasonable 
		// 	- if order is in right format

		this.numDims = dims.length;
		int size = 1;
		for(int d = 0; d < this.numDims; d++)
		{
			size *= dims[d];
		}
		this.size = size;
		this.shape = new int[this.numDims];
		String typeName = array[0][0][0].getClass().getSimpleName();
		switch(typeName)
		{
			case "Short": { }
			case "Integer": { }
			case "Long":
			{
				Int64 t = new Int64();
				this.dtype = new DType(t);
				break;
			}
			case "Float": { }
			case "Double":
			{
				Float64 t = new Float64();
				this.dtype = new DType(t);
				break;
			}
			default:
				throw new IllegalArgumentException("bad input Array type"); // this line is not likely to be checked
		}
		this.s_k = new int[this.numDims];
		calSisParams(this.s_k, this.shape, this.dtype.itemsize, dims, this.order);

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		if(this.dtype.NAME.equals("NumJ.Int64"))
		{
			for(N[][] array2d : array)
			{
				for(N[] array1d : array2d)
				{
					for(N ele : array1d)
					{
						byte[] next = this.dtype.toByte(ele.longValue());
						result.add(next);						
					}
				}
			}
		}
		else
		{
			for(N[][] array2d : array)
			{
				for(N[] array1d : array2d)
				{
					for(N ele : array1d)
					{
						byte[] next = this.dtype.toByte(ele.doubleValue());
						result.add(next);						
					}
				}
			}
		}

		this.DATA_POOL = Utils.concatBytesArr(result);
	}

	// public static NDArray arange();
	public static NDArray zeros(int[] dims, DType dtype, Character order)
	{
		order = order != null ? order : 'C';
		int numDims = dims.length;
		int size = 1;
		for(int d = 0; d < numDims; d++)
		{
			size *= dims[d];
		}
		NDArray ndarr = new NDArray(size, numDims);
		ndarr.dtype = dtype;
		ndarr.order = order.equals('C') ? 0 : 1;

		calSisParams(ndarr.s_k, ndarr.shape, ndarr.dtype.itemsize, dims, ndarr.order);

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		switch(ndarr.dtype.NAME)
		{
			case "NumJ.Int32":	
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.INT_2_BYTE(0);
					result.add(next);
				}
				break;
			}
			case "NumJ.Int16":
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.SHORT_2_BYTE((short)0);
					result.add(next);
				}
				break;
			}
			case "NumJ.Int64":
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.LONG_2_BYTE((long)0);
					result.add(next);
				}
				break;
			}
			case "NumJ.Float32":
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.FLOAT_2_BYTE((float)0.0);
					result.add(next);
				}
				break;
			}
			case "NumJ.Float64":
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.DOUBLE_2_BYTE(0.0);
					result.add(next);
				}
				break;
			}
			default:
				throw new IllegalArgumentException("bad input Array type"); // this line is not likely to be checked
		}
		ndarr.DATA_POOL = Utils.concatBytesArr(result);	
		return ndarr;
	}

	public static NDArray ones(int[] dims, DType dtype, Character order)
	{
		order = order != null ? order : 'C';
		int numDims = dims.length;
		int size = 1;
		for(int d = 0; d < numDims; d++)
		{
			size *= dims[d];
		}
		NDArray ndarr = new NDArray(size, numDims);
		ndarr.dtype = dtype;
		ndarr.order = order.equals('C') ? 0 : 1;

		calSisParams(ndarr.s_k, ndarr.shape, ndarr.dtype.itemsize, dims, ndarr.order);

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		switch(ndarr.dtype.NAME)
		{
			case "NumJ.Int32":	
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.INT_2_BYTE(1);
					result.add(next);
				}
				break;
			}
			case "NumJ.Int16":
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.SHORT_2_BYTE((short)1);
					result.add(next);
				}
				break;
			}
			case "NumJ.Int64":
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.LONG_2_BYTE((long)1);
					result.add(next);
				}
				break;
			}
			case "NumJ.Float32":
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.FLOAT_2_BYTE((float)1.0);
					result.add(next);
				}
				break;
			}
			case "NumJ.Float64":
			{
				for(int x = 0; x < ndarr.size; x++)
				{
					byte[] next = Utils.DOUBLE_2_BYTE(1.0);
					result.add(next);
				}
				break;
			}
			default:
				throw new IllegalArgumentException("bad input Array type"); // this line is not likely to be checked
		}
		ndarr.DATA_POOL = Utils.concatBytesArr(result);	
		return ndarr;		
	}

	// Indexing & Slicing
	/**
	* Core operation of this structure
	* @param index 	new index of this NDArray
	******/
	public <N extends Number> N idx(int... index)
	{
		int offset = _idx(index);
		return this.dtype.parseByte(this.DATA_POOL, offset);
	}
	// public <N extends Number> N[] slc()
	// {

	// }
	protected int _idx(int... index)
	{
		// check if s_k initlized
		// check correctness of dimension
		int offset = 0;
		int i = 0;
		for(int idx : index)
		{
			offset += this.s_k[i] * idx;
			i++;
		}
		return offset;
	}
	// // only support continous slicing
	// private int[] _slc(int[]... index)
	// {
	// 	// safety check
	// 	// if dimension fits the number of input
	// 	int size = 0;
	// 	for(int[] idx : index)
	// 	{
	// 		size += (idx[idx.length - 1] - idx[0] + 1);
	// 	}
	// 	int[] offsets = new int[size];
	// 	for(int[] idx : index)
	// 	{

	// 	}
	// }
	/**
	* There's no ganrantee that the DATA_POOL will follow the index order
	*
	*/



	// Representation
	/**
	*	[[[1. 2. 3.]
	*	  [4. 5. 6.]
	*	  [7. 8. 9.]]
	*
	*	 [[1. 2. 3.]
	*	  [4. 5. 6.]
	*	  [7. 8. 9.]]]
	* the same with numpy array
	*/
	public void print()
	{	
		System.out.println(Utils._repr(this, false));
	}
	public void repr()
	{
		System.out.println(Utils._repr(this, true));
	}

	// Reshaping
	/**
	* This is a in-place operation!
	*
	*/
	public void reshape(int[] dims) 
	{
		// safety check
		int size = 1;
		for(int i = 0; i < dims.length; i++)
		{
			size *= dims[i];
		}
		if(size != this.size)
		{
			String shape = Arrays.toString(dims);
			String mesg = String.format("Can't reshape size %d to shape %s", this.size, shape);
			throw new IllegalArgumentException(mesg);
		}
		// update s_k
		int[] newshape = new int[dims.length];
		calSisParams(this.s_k, newshape, this.dtype.itemsize, dims, this.order);

		// update shape
		this.shape = newshape;

		// update numDims
		this.numDims = dims.length;
	}
	public static NDArray reshape(NDArray ndarr, int[] dims)
	{
		// safety check
		int size = 1;
		for(int i = 0; i < dims.length; i++)
		{
			size *= dims[i];
		}
		if(size != ndarr.size)
		{
			String shape = Arrays.toString(dims);
			String mesg = String.format("Can't reshape size %d to shape %s", ndarr.size, shape);
			throw new IllegalArgumentException(mesg);
		}

		NDArray newarr = deepCopy(ndarr);
		// update s_k
		int[] newshape = new int[dims.length];
		calSisParams(newarr.s_k, newshape, newarr.dtype.itemsize, dims, newarr.order);

		// update shape
		newarr.shape = newshape;

		// update numDims
		newarr.numDims = dims.length;

		return newarr;
	}

	// Data Conversion
	// public static Arrays toArray();

	// Copy
	public static NDArray deepCopy(NDArray ndarr)
	{
		NDArray newarr = new NDArray(
			ndarr.shape, 
			ndarr.dtype, 
			ndarr.size, 
			ndarr.numDims, 
			ndarr.DATA_POOL, 
			ndarr.s_k, 
			ndarr.order
		);
		return newarr;
	}
	public static NDArray shallowCopy(NDArray ndarr)
	{
		return ndarr;
	}

	// Type converstion
	// public astype(DType dtype)
	// {

	// }
	// public static NDArray astype(Number dtype); // deep copy

	// // airthmetic operations
	// public static NDArray add();
	// public static NDArray extract();
	// public static NDArray multiple();
	// public static NDArray divide()

	// matrix operations
	/**
	*	Assuming only working on the Float64 dtype
	*	Only support 1-
	*
	*/
	// public NDArray dot(NDArray ndar)
	// {
	// 	// ndarr and this must share the same dimensions
	// 	// 

	// }

	// public static NDArray T();
	// vstack
	// hstack
	// broadcast

	// // basic functions
	// public static NDArray log();
	// public static NDArray exp();

	// // util tools
	// public static NDArray sum();
	// public static NDArray max();
	// public static NDArray min();



	// Help Functions
	private static int truncatedProduct(int start, int end, int numDims, int[] shape)
	{
		int s_k = 1;
		if(start >= numDims)
		{
			return 1;
		}
		for(int j = start; j <= end; j++)
		{
				s_k *= shape[j];
		}
		return s_k;
	}

	// calculate stride idx scheme params
	/**
	* @param s_k: the s_k you wish to cal
	* @param shape: the shape you wish to get
	* @param itemsize: the itemsize you've already caled
	* @param dims: the dims you got
	* @param order: the order you got
	*/
	private static void calSisParams(int[] s_k,  int [] shape, int itemsize, int[] dims, int order)
	{
		// safty check
		int numDims = dims.length;
		System.arraycopy(dims, 0, shape, 0, numDims);
		if(order==0)
		{
			for(int k = 0; k < numDims; k++)
			{
				s_k[k] = truncatedProduct(k+1, numDims-1, numDims, shape) * itemsize;
			}
		}
		else
		{
			for(int k = 0; k < numDims; k++)
			{
				s_k[k] = truncatedProduct(0, k-1, numDims, shape) * itemsize;
			}
		}	
	}

	public static void main(String [] vargs)
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
		// Long i = ndarr.idx(1,2);
		// System.out.println(ndarr.dtype.NAME);
		// System.out.println(i);
		Int16 type = new Int16();
		DType dtype = new DType(type);
		NDArray ndarr_self = new NDArray(dims1, dtype, null);
		// System.out.println(ndarr_self.idx(1,1));
		// System.out.println(ndarr_self.dtype.NAME);
		NDArray zeros = NDArray.zeros(dims1, dtype, null);
		NDArray ones = NDArray.ones(dims1, dtype, null);
		// zeros.repr();
		// ones.repr();
		// ndarr1.repr();
		// ndarr2.repr();
		ndarr3.repr();
		// ndarr_self.repr();
		int[] newdims = {12};
		ndarr3.reshape(newdims);
		ndarr3.repr();
		int[] newnewdims = {2,6};
		NDArray ndarr4 = reshape(ndarr3, newnewdims);
		ndarr4.repr();
	}

}