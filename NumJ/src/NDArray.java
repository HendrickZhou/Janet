/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

package org.NumJ

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
    public int size;
    public DType dtype;
    private byte[] DATA_POOL; // data storage
    private int[] s_k;
    private int order; // 0 for 'C', 1 for 'F'
    public numDims;

	// Constructor
	// core construction method
	/**
	* Randomly generated
	* @param dtype 	data type of this NDArray, only support java non-primitive type
	*/
	private static NDArray ndarray(Arrays shape, Number dtype) // java builtin data type for the simplification(java.lang.Number)
	{



	}



	private static void truncatedProduct(int start, int end)
	{
		int s_k = 1;
		if(start >= this.numDims)
		{
			return 1;
		}
		for(int j = start; j <= end; j++)
		{
				s_k *= this.shape[j];
		}
		return s_k;
	}

	// calculate stride idx scheme params
	private static void calSisParams(int[] dims, String order)
	{
		System.arraycopy(dims, 0, this.shape, 0, this.numDims);
		if(order == 'C')
		{
			for(int k = 0; k < this.numDims; k++)
			{
				this.s_k[k] = truncatedProduct(k+1, this.numDims-1);
			}
		}
		else
		{
			for(int k = 0; k < this.numDims; k++)
			{
				this.s_k[k] = truncatedProduct(0, k-1)
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
	public NDArray(int[] array, int[] dims, String order)
	{
		order = order != null ? order : 'C';
		// safty check
		//	- if input array  is reasonable
		// 	- if dims are right
		//	- if shape is reasonable 
		// 	- if order is in right format

		this.numDims = dims.length;
		this.s_k = new int[this.numDims];
		int size = 1
		for(int d = 0; d < this.numDims; d++)
		{
			size *= dims[d];
		}
		this.size = size;
		calSisParams(dims, order);
		
		if(this.DATA_POOL == null)
		{
			this.DATA_POOL = new byte[this.size * dtype.itemsize];
		}
		
		for(int x = 0; x < array.length; x++)
		{
			this.DATA_POOL
		}


	}

	public static NDArray(int[][] array, int[] dims, String order)
	{

	}
	public static NDArray(int[][][] array, int[] dims, String order)

	public static NDArray(long[] array, int[] dims, String order)
	public static NDArray(long[][] array, int[] dims, String order)
	public static NDArray(long[][][] array, int[] dims, String order)

	public static NDArray(short[] array, int[] dims, String order)
	public static NDArray(short[][] array, int[] dims, String order)
	public static NDArray(short[][][] array, int[] dims, String order)

	public static NDArray(float[] array, int[] dims, String order)
	public static NDArray(float[][] array, int[] dims, String order)
	public static NDArray(float[][][] array, int[] dims, String order)

	public static NDArray(double[] array, int[] dims, String order)
	public static NDArray(double[][] array, int[] dims, String order)
	public static NDArray(double[][][] array, int[] dims, String order)


	// Indexing & Slicing
	/**
	* Core operation of this structure
	* @param index 	new index of this NDArray
	******/
	public static idx(Arrays index);
	{

	}
	public static slc(Arrays start, Arrays end);

	// public static Arrays toArray();

	public static NDArray zeros();
	public static NDArray ones();
	public static NDArray arange();

	// matrix operation
	// reshaping
	public static NDArray reshape();
	// vstack
	// hstack

	// representation
	public static void repr();

	// Type converstion
	// astype()
	public static void asType(Number dtype); // internal
	public static NDArray toType(Number dtype); // deep copy



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


	public static void main()
	{

	}

}