/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

// package org.NumJ

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

    private byte[] DATA_POOL; // data storage
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
	NDArray(int size, int numDims)
	{
		this.numDims = numDims;
		this.size = size;
		this.s_k = new int[numDims];
		this.shape = new int[numDims];
		this.DATA_POOL = new byte[size];
		this.dtype = new DType();
	}
	/**
	* Randomly generated
	* By default, int/float will be between 0 and 100
	* @param dtype 	data type of this NDArray, only support java non-primitive type
	*/
	NDArray(int[] dims, DType dtype, Character order)
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
		calSisParams(this.s_k, this.shape, this.dtype.itemsize, dims, order, this.numDims);


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
	<N extends Number> NDArray(N[] array, int[] dims, Character order)
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
		calSisParams(this.s_k, this.shape, this.dtype.itemsize, dims, order, this.numDims);

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
	// public <N extends Number> NDArray(N[][] array, int[] dims, String order);
	// public <N extends Number> NDArray(N[][][] array, int[] dims, String order);


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

		calSisParams(ndarr.s_k, ndarr.shape, ndarr.dtype.itemsize, dims, order, ndarr.numDims);

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

		calSisParams(ndarr.s_k, ndarr.shape, ndarr.dtype.itemsize, dims, order, ndarr.numDims);

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
	private int _idx(int... index)
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

	// public static Arrays toArray();


	// matrix operation
	// reshaping
	// public static NDArray reshape();
	// vstack
	// hstack

	// representation
	// public static void repr();

	// Type converstion
	// public astype(DType dtype)
	// {

	// }
	// public static NDArray astype(Number dtype); // deep copy



	// Iterating

	// Basic Airthmetic Operation



	// // airthmetic operations
	// public static NDArray add();
	// public static NDArray extract();
	// public static NDArray multiple();
	// public static NDArray divide()

	// // matrix operations
	// public static NDArray dot();
	// public static NDArray T();

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
	private static void calSisParams(int[] s_k,  int [] shape, int itemsize, int[] dims, Character order, int numDims)
	{
		// safty check
		System.arraycopy(dims, 0, shape, 0, numDims);
		if(order.equals('C'))
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
		Integer[] java_array={1,2,3,55,100,2000};
		int[] dims = {2,3, 1};
		NDArray ndarr = new NDArray(java_array, dims, 'C');
		Long i = ndarr.idx(1,2);
		// System.out.println(ndarr.dtype.NAME);
		// System.out.println(i);
		Int16 type = new Int16();
		DType dtype = new DType(type);
		NDArray ndarr_self = new NDArray(dims, dtype, null);
		// System.out.println(ndarr_self.idx(1,1));
		// System.out.println(ndarr_self.dtype.NAME);
		NDArray zeros = NDArray.zeros(dims, dtype, null);
		NDArray ones = NDArray.ones(dims, dtype, null);
		zeros.repr();
		ones.repr();
		ndarr.repr();
		ndarr_self.repr();
	}

}