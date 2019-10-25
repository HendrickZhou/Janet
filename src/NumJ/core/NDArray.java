/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
package NumJ.core;

import NumJ.type.BaseType;
import NumJ.type.Float64;
import NumJ.type.Float32;
import NumJ.type.Int32;
import NumJ.type.Int16;
import NumJ.type.Int64;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
/**
*	This data structure is meant be flexiable for matrix operation
*/
public class NDArray
{
	// fields
    protected int[] shape; //d_k
    protected DType dtype;
    protected int size;
    protected int numDims;

    protected byte[] DATA_POOL; // data storage
    protected int[] s_k;
    protected int order = 0; // 0 for 'C', 1 for 'F'    

    /**
    * This is meant for outside users
    * The implementation of this library aren't expected to use these.
    * They can call the attr directly
    **/
    public int[] shape()
    {
    	return getter_shape();
    }
    public int size()
    {
    	return size;
    }
    public int numDims()
    {
    	return numDims;
    }
    public DType type()
    {
    	return getter_dtype();
    }
    public Character order()
    {
    	int o =  getter_order();
    	if(o == 0)
    	{
    		return 'C';
    	}
    	else
    	{
    		return 'F';
    	}
    }

    // propose of these method are to deep copying the attrs
    // inner impelmentation should use these methods
    protected int [] getter_shape()
    {
    	return Utils.deepCopyIntArray(shape);
    }
    protected DType getter_dtype()
    {
    	return dtype;
    }
    protected byte[] getter_DATAPOOL()
    {
    	return Utils.deepCopyByteArray(DATA_POOL);
    }
    protected byte get_DP_at(int offset)
    {
    	return this.DATA_POOL[offset];
    }
    protected int getter_order()
    {
    	return order;
    }
    //
    protected void setter_dtype(DType dtype)
    {
		this.dtype = dtype;
    }
    protected void setter_shape(int [] shape) // make sure called after dtype, or after if astype
    {
    	int newsize = 1;
    	for(int i = 0; i<shape.length;i++)
    	{
    		newsize *= shape[i];
    	}
    	if(this.shape != null)
    	{
	    	if(newsize != this.size)
	    	{
	    		throw new IllegalArgumentException("new shape should assure the same data size");
	    	}
	    	// if(Arrays.equals(this.shape, shape))
    	}
    	this.shape = Utils.deepCopyIntArray(shape);
    	this.numDims = shape.length;
    	this.size = newsize;
    	this.s_k = this.calSisParams(this.dtype.itemsize, this.shape, this.order);
    }
    // be sure to call set shape first
    // or at least make sure you know the shape ain't changed
    protected void setter_DATAPOOL(byte[] data)
    {
    	this.DATA_POOL = Utils.deepCopyByteArray(data);
    }
    protected void modify_DATAPOOL(int offset, byte newval)
    {
    	this.DATA_POOL[offset] = newval;
    }
    protected void setter_order(int order)
    {
    	this.order = order;
    }
    // should have a set order here, but since order is just a toy param,
    // we leave it for the future use.

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
	public NDArray() { }
	// public NDArray(int size, int numDims)
	// {
	// 	this.numDims = numDims;
	// 	this.size = size;
	// 	this.s_k = new int[numDims];
	// 	this.shape = new int[numDims];
	// 	this.DATA_POOL = new byte[size];
	// 	this.dtype = new DType();
	// }
	public NDArray(int[] shape, DType dtype, byte[] DATA_POOL, int order)
	{
		setter_dtype(dtype);
		setter_shape(shape);
		setter_DATAPOOL(DATA_POOL);
		setter_order(order);
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

		// this.numDims = dims.length;
		// int size = 1;
		// for(int d = 0; d < this.numDims; d++)
		// {
		// 	size *= dims[d];
		// }
		// this.size = size;
		// this.shape = new int[this.numDims];
		// this.s_k = new int[this.numDims];
		// calSisParams(this.s_k, this.shape, this.dtype.itemsize, dims, this.order);

		this.setter_shape(dims);

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

		// this.numDims = dims.length;
		// int size = 1;
		// for(int d = 0; d < this.numDims; d++)
		// {
		// 	size *= dims[d];
		// }
		// this.size = size;
		// this.shape = new int[this.numDims];
		String typeName = array[0].getClass().getSimpleName();
		switch(typeName)
		{
			case "Short": { }
			case "Long": { }
			case "Integer": 
			{
				Int32 t = new Int32();
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
		// this.s_k = new int[this.numDims];
		// calSisParams(this.s_k, this.shape, this.dtype.itemsize, dims, this.order);
		this.setter_shape(dims);

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		if(this.dtype.NAME.equals("NumJ.Int32"))
		{
			for(int x = 0; x < array.length; x++)
			{
				byte[] next = this.dtype.toByte(array[x].intValue());
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

		String typeName = array[0][0].getClass().getSimpleName();
		switch(typeName)
		{
			case "Short": { }
			case "Long": { }
			case "Integer": 
			{
				Int32 t = new Int32();
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

		this.setter_shape(dims);

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		if(this.dtype.NAME.equals("NumJ.Int32"))
		{
			for(int x = 0; x < array.length; x++)
			{
				for(int y = 0; y < array[0].length; y++)
				{
					byte[] next = this.dtype.toByte(array[x][y].intValue());
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

		String typeName = array[0][0][0].getClass().getSimpleName();
		switch(typeName)
		{
			case "Short": { }
			case "Long": { }
			case "Integer": 
			{
				Int32 t = new Int32();
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

		this.setter_shape(dims);

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		if(this.dtype.NAME.equals("NumJ.Int32"))
		{
			for(N[][] array2d : array)
			{
				for(N[] array1d : array2d)
				{
					for(N ele : array1d)
					{
						byte[] next = this.dtype.toByte(ele.intValue());
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
		NDArray ndarr = new NDArray();
		ndarr.order = order.equals('C') ? 0 : 1;
		ndarr.dtype = dtype;
		ndarr.setter_shape(dims);
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
		NDArray ndarr = new NDArray();
		ndarr.dtype = dtype;
		ndarr.order = order.equals('C') ? 0 : 1;
		ndarr.setter_shape(dims);

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
	// make sure you know the type when using the idx!!!!!!!!!!!!!
	public int idx_int(int... index)
	{
		int offset = _idx(index);
		return this.dtype.parseByteInt(this.DATA_POOL, offset);
	}
	public long idx_long(int... index)
	{
		int offset = _idx(index);
		return this.dtype.parseByteLong(this.DATA_POOL, offset);
	}
	public short idx_short(int... index)
	{
		int offset = _idx(index);
		return this.dtype.parseByteShort(this.DATA_POOL, offset);
	}
	public float idx_float(int... index)
	{
		int offset = _idx(index);
		return this.dtype.parseByteFloat(this.DATA_POOL, offset);
	}
	public double idx_double(int... index)
	{
		int offset = _idx(index);
		return this.dtype.parseByteDouble(this.DATA_POOL, offset);
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
	public void repr(boolean show_shape)
	{
		repr();
		show_shape();
	}
	public void show_shape()
	{
		System.out.println("Shape=" + Arrays.toString(this.getter_shape()));
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
		this.shape = Utils.deepCopyIntArray(dims);
		this.numDims = dims.length;
		this.s_k = calSisParams(this.dtype.itemsize, dims, this.order);
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
		newarr.setter_shape(dims);

		return newarr;
	}

	// Data Conversion
	// public static Arrays toArray();

	// Copy
	/**
	*	Well actually this copy method won't be compeletly deep
	*	The dtype is not deep copied since we need serilization to truly deeply copy a object
	*	But since the dtype has the `final` attribute so we're probably good here.
	*/
	public static NDArray deepCopy(NDArray ndarr)
	{
		NDArray newarr = new NDArray(
			ndarr.getter_shape(), 
			ndarr.getter_dtype(), 
			ndarr.getter_DATAPOOL(), 
			ndarr.getter_order()
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




	/**************************
	* Airthmetic and Matrix operations
	**************************/

	// // airthmetic operations
	public NDArray add(NDArray ndarr)
	{
		return Matrix.matadd(this, ndarr);
	}
	public NDArray add(int scalar)
	{
		return Matrix.add_scalar(this, scalar);
	}
	public NDArray add(double scalar)
	{
		return Matrix.add_scalar(this, scalar);
	}
	public NDArray subtract(NDArray ndarr)
	{
		return add(Matrix.minus(ndarr));
	}
	public NDArray subtract(int scalar)
	{
		return add(-scalar);
	}
	public NDArray subtract(double scalar)
	{
		return add(-scalar);
	}


	// matrix operations
	/**
	*	Assuming only working on the Float64 dtype
	*	We're expected to futher the function on any dimension
	*	But now we only focus on the <= 2d operation
	*	1. Matrix opertion below 2d
	*	2. Scalar and Matrix operation
	* Warning: DONT USE FRACTION!!!
	*/
	public NDArray dot(NDArray ndarr)
	{
		return Matrix.matmul(this, ndarr);
	}
	public NDArray dot(double scalar)
	{
		return Matrix.scalar(this, scalar);
	}
	public NDArray dot(int scalar)
	{
		return Matrix.scalar(this, scalar);
	}



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
	// protected static void calSisParams(int[] s_k,  int [] shape, int itemsize, int[] dims, int order)
	protected static int[] calSisParams(int itemsize, int[] dims, int order)
	{
		// safty check
		int numDims = dims.length;
		int[] s_k = new int[numDims];
		if(order==0)
		{
			for(int k = 0; k < numDims; k++)
			{
				s_k[k] = truncatedProduct(k+1, numDims-1, numDims, dims) * itemsize;
			}
		}
		else
		{
			for(int k = 0; k < numDims; k++)
			{
				s_k[k] = truncatedProduct(0, k-1, numDims, dims) * itemsize;
			}
		}
		return s_k;	
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
		Double[] arr1d={1.0,2.0,3.0,55.0,100.0,2000.0};

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
		zeros.repr();
		ones.repr();
		ndarr1.repr();
		ndarr2.repr();
		ndarr3.repr();
		// ndarr_self.repr();
		int[] newdims = {12};
		ndarr3.reshape(newdims);
		ndarr3.repr();
		System.out.println(Arrays.toString(ndarr3.shape));
		int [] onedim = {10};
		NDArray oned = new NDArray(onedim, dtype, null);
		oned.repr();
		int[] newnewdims = {2,6};
		NDArray ndarr4 = reshape(ndarr3, newnewdims);
		ndarr4.repr();
		System.out.println(ndarr4.DATA_POOL.length);

		ndarr1.dot(0.33).repr(true);
	}

}