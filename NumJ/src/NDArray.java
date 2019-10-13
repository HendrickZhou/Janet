/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
import java.util.Arrays;
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



	public static Array shape;

	// Constructor
	// core construction method
	/**
	* Randomly generated
	*/
	private static NDArray ndarray(Arrays shape, Number dtype) // java builtin data type for the simplification(java.lang.Number)
	{



	}


	/**
	* Only support Array with regular dimensions, if not will throw the exception
	* Only support Simple Array with fixed dimension 1d, 2d & 3d
	* @param input 	Arrays with regular dimensions
	* @param dtype 	data type of this NDArray, only support java non-primitive type
	* @param order 	order of layout, 'C'(C-style) or 'F'(Fortran style)
	* @return 		NDArray	
	*/
	public static NDArray( input, Number dtype, String order)
	{
		// safty check
		//	- if input array  is reasonable
		//	- if shape is reasonable 
		// 	- if order is in right format

		order = order != null ? order : 'C';
		// re-indexing

		if(order == 'C')
		{

		}
		else
		{

		}


		// set the shape, strides etc


	}

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
