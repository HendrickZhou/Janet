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
	// public static NDArray arange();
	// public static NDArray zeros();
	// public static NDArray ones();
	/**
	* Randomly generated
	* @param dtype 	data type of this NDArray, only support java non-primitive type
	*/
	private NDArray(int[] dims, DType dtype, Character order)
	{
		order = order != null ? order : 'C';
		this.order = order.equals('C') ? 0 : 1;
		// safty check

		this.numDims = dims.length;
		int size = 1;
		for(int d = 0; d < this.numDims; d++)
		{
			size *= dims[d];
		}
		this.size = size;
		this.shape = new int[this.numDims];
		this.s_k = new int[this.numDims];
		calSisParams(this.s_k, this.shape, dims, order, this.numDims);

		this.dtype = dtype;

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		switch(this.dtype.NAME)
		{
			case "NumJ.Int32":	
			{
				for(int x = 0; x < this.size; x++)
				{
					byte[] next = this.dtype.toByte(random.nextInt(100)); // will be improved
					result.add(next);
				}
				break;
			}
			case "NumJ.Int16":
			{
				break;
			}
			case "NumJ.Int64":
			{
				break;
			}
			case "NumJ.Float32":
			{
				break;
			}
			case "NumJ.Float64":
			{
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
		this.s_k = new int[this.numDims];
		calSisParams(this.s_k, this.shape, dims, order, this.numDims);
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

	// Indexing & Slicing
	/**
	* Core operation of this structure
	* @param index 	new index of this NDArray
	******/
	public <N extends Number> N idx(int... index)
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
		offset *= this.dtype.itemsize;
		return this.dtype.parseByte(this.DATA_POOL, offset);
	}
	// public static void slc(Arrays start, Arrays end);



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
	// public static NDArray random();



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
	private static void calSisParams(int[] s_k,  int [] shape, int[] dims, Character order, int numDims)
	{
		// safty check
		System.arraycopy(dims, 0, shape, 0, numDims);
		if(order.equals('C'))
		{
			for(int k = 0; k < numDims; k++)
			{
				s_k[k] = truncatedProduct(k+1, numDims-1, numDims, shape);
			}
		}
		else
		{
			for(int k = 0; k < numDims; k++)
			{
				s_k[k] = truncatedProduct(0, k-1, numDims, shape);
			}
		}	
	}

	public static void main(String [] vargs)
	{
		Integer[] java_array={1,2,3,55,100,2000};
		int[] dims = {2,3};
		NDArray ndarr = new NDArray(java_array, dims, 'C');
		// Utils.reprBytes(ndarr.DATA_POOL, ndarr.DATA_POOL.length);
		Long i = ndarr.idx(1,2);
		System.out.println(i);
		Int32 type = new Int32();
		DType dtype = new DType(type);
		NDArray ndarr_self = new NDArray(dims, dtype, null);
		System.out.println(ndarr_self.idx(1,1));
	}

}