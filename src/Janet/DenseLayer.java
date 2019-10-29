package Janet;

import NumJ.core.*;
import NumJ.type.*;

public class DenseLayer
{
	NDArray W;
	NDArray b;
	NDArray cached;
	Activation act;
	DenseLayer(int[] shape)
	{
		if(shape.length != 2)
		{
			throw new IllegalArgumentException("illegal shape");
		}
		DType type = new DType(new Float64());
		int[] shape_w = {shape[1], shape[0]}; // we can use T but it's faster
		this.W = new NDArray(shape_w, type, null);
		int[] shape_b = {shape[1]};
		this.b = new NDArray(shape_b, type, null);
		this.act = new Activation("Sigmod");
	}
	DenseLayer(int[] shape, String type)
	{
		if(shape.length != 2)
		{
			throw new IllegalArgumentException("illegal shape");
		}
		DType type = new DType(new Float64());
		int[] shape_w = {shape[1], shape[0]}; // we can use T but it's faster
		this.W = new NDArray(shape_w, type, null);
		int[] shape_b = {shape[1]};
		this.b = new NDArray(shape_b, type, null);
		this.act = new Activation(type);
	}
	// DenseLayer(int[] shape, NDArray W, NDArray b) // other init methods

	public static void forward(NDArray X)
	{
		this.cached = NDArray.deepCopy(X);
		// dot is not necessary or capable of in-place ops
		// so we just create a new var to make it more readable
		NDArray linear_ops = this.W.dot(X).add(this.b); 

		this.Activation.forward(linear_ops);
	}

	public static void backward(NDArray )

	// public static backward()
	public static void main(String [] argv)
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
		NDArray ndarr_self = new NDArray(dims1, dtype, 'C');
		// System.out.println(ndarr_self.idx(1,1));
		// System.out.println(ndarr_self.dtype.NAME);
		NDArray zeros = NDArray.zeros(dims1, dtype, 'C');
		NDArray ones = NDArray.ones(dims1, dtype, 'C');
		zeros.repr();
		ones.repr();
		ndarr1.repr();
		ndarr2.repr();
		ndarr3.repr();
		// ndarr_self.repr();
		int[] newdims = {12};
		ndarr3.reshape(newdims);
		ndarr3.repr();
		int [] onedim = {10};
		NDArray oned = new NDArray(onedim, dtype, 'C');
		oned.repr();
		int[] newnewdims = {2,6};
		NDArray ndarr4 = NDArray.reshape(ndarr3, newnewdims);
		ndarr4.repr();
		ndarr1.dot(0.33).repr(true);
	}
}