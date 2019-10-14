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
    // private static Random random;
    // private static long seed;

    // static {
    //     seed = System.currentTimeMillis();
    //     random = new Random(seed);
    // }

    public int[] shape; //d_k
    public DType dtype;
    public int size;
    public int numDims;

    private byte[] DATA_POOL; // data storage
    private int[] s_k;
    private int order = 0; // 0 for 'C', 1 for 'F'    

	// Constructor
	// core construction method
	/**
	* Randomly generated
	* @param dtype 	data type of this NDArray, only support java non-primitive type
	*/
	// private static NDArray ndarray(Arrays shape, Number dtype); // java builtin data type for the simplification(java.lang.Number)


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
	private void calSisParams(int[] s_k,  int [] shape, int[] dims, Character order, int numDims)
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


	/**
	* Only support Array with regular dimensions, if not will throw the exception
	* Only support Simple Array with fixed dimension 1d, 2d & 3d
	* Only receive primitive java type: short, int, long, float, double
	* @param input 	Arrays with regular dimensions
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
		this.s_k = new int[this.numDims];
		int size = 1;
		for(int d = 0; d < this.numDims; d++)
		{
			size *= dims[d];
		}
		this.size = size;
		this.shape = new int[this.numDims];
		calSisParams(this.s_k, this.shape, dims, order, this.numDims);
		// System.out.println(this.s_k);
		String typeName = array[0].getClass().getSimpleName();
		switch(typeName)
		{
			case "Integer":	
			{
				Int32 t = new Int32();
				this.dtype = new DType(t);
				break;
			}
			case "Short":
			{
				Int16 t = new Int16();
				this.dtype = new DType(t);
				break;
			}
			case "Long":
			{
				Int64 t = new Int64();
				this.dtype = new DType(t);
				break;
			}
			case "Float":
			{
				Float32 t = new Float32();
				this.dtype = new DType(t);
				break;
			}
			case "Double":
			{
				Float64 t= new Float64();
				this.dtype = new DType(t);
				break;
			}
			default:
				throw new IllegalArgumentException("bad input Array type"); // this line is not likely to be checked
		}
		
		// System.out.println(this.dtype);
		// System.out.println((Int32)this.dtype.name);
		ArrayList<byte[]> result = new ArrayList<byte[]>();
		for(int x = 0; x < array.length; x++)
		{
			byte[] next = this.dtype.toByte(array[x]);
			result.add(next);
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
	// public static void idx(Arrays index);
	// public static void slc(Arrays start, Arrays end);

	// public static Arrays toArray();

	// public static NDArray zeros();
	// public static NDArray ones();
	// public static NDArray arange();

	// matrix operation
	// reshaping
	// public static NDArray reshape();
	// vstack
	// hstack

	// representation
	// public static void repr();

	// Type converstion
	// astype()
	// public static void asType(Number dtype); // internal
	// public static NDArray toType(Number dtype); // deep copy



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


	public static void main(String [] vargs)
	{
		Integer[] java_array={1,2,3,55,100,2000};
		int[] dims = {2,3};
		NDArray ndarr = new NDArray(java_array, dims, 'C');
		Utils.reprBytes(ndarr.DATA_POOL, ndarr.DATA_POOL.length);
	}

}