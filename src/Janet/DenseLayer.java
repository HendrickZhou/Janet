package Janet;

import NumJ.core.*;
import NumJ.type.*;

public class DenseLayer
{
	NDArray W;
	NDArray b;
	NDArray cached;
	Activation activation;
	int step;
	int l_1; 	// number of nodes from last layer
	int l;		// number of nodes the next layer
	int m;		// number of datas, 0 default
	double learning_rate;
	DenseLayer(int in, int out)
	{
		this.l_1 = in;
		this.l = out;

		int[] shape_w = {l, l_1};
		DType type = new DType(new Float64());
		this.W = new NDArray(shape_w, type, null);
		int[] shape_b = {l, 1};
		this.b = new NDArray(shape_b, type, null);
		this.activation = new Activation("Sigmod");
		this.step = 0;
		this.learning_rate = 0.1;
	}
	DenseLayer(int in, int out, String activation, double learning_rate)
	{
		this.l_1 = in;
		this.l = out;

		int[] shape_w = {l, l_1};
		DType type = new DType(new Float64());
		this.W = new NDArray(shape_w, type, null);
		int[] shape_b = {l, 1}; // broadcast dont support 1d array!!
		this.b = new NDArray(shape_b, type, null);
		this.activation = new Activation(activation);
		this.step = 0;
		this.learning_rate = learning_rate;
	}
	DenseLayer(int[] shape, String activation)
	{
		if(shape.length != 2)
		{
			throw new IllegalArgumentException("wrong shape");
		}
		this.l_1 = shape[0];
		this.l = shape[1];

		int[] shape_w = {l, l_1};
		DType type = new DType(new Float64());
		this.W = new NDArray(shape_w, type, null);
		int[] shape_b = {l, 1}; // broadcast dont support 1d array!!
		this.b = new NDArray(shape_b, type, null);
		this.activation = new Activation(activation);
		this.step = 0;
		this.learning_rate = 0.1;
	}
	// DenseLayer(int[] shape, NDArray W, NDArray b) // other init methods
	public void set_m(int m)
	{
		this.m = m;
	}
	public void set_learning_rate(double l)
	{
		this.learning_rate = l;
	}
	// not in-place ops
	public NDArray forward(NDArray X)
	{
		if(X.getter_shape()[0] != this.l_1 || X.getter_shape().length != 2)
		{
			throw new IllegalArgumentException("illegal input data size");
		}
		if(this.m == 0) // uninitialized
		{
			this.m = X.getter_shape()[1];
		}
		// if(this.step != 0)
		// {
		// 	throw new IllegalArgumentException("Last training step hasn't finished yet!! call backward first");
		// }
		this.cached = NDArray.deepCopy(X);
		// dot is not necessary or capable of in-place ops
		// so we just create a new var to make it more readable
		// X.repr(true);
		// W.repr(true);
		// b.repr(true);
		NDArray linear_ops = this.W.dot(X);
		// linear_ops.repr(true);
		// this.b.repr(true);
		linear_ops = linear_ops.add(this.b); 
		// linear_ops.repr(true);
		NDArray A = this.activation.forward(linear_ops);

		// this.step++;
		return A;
	}

	public NDArray backward(NDArray dA)
	{
		// this.step--;
		// if(this.step != 0)
		// {
		// 	throw new IllegalArgumentException("call forward first");
		// }
		// if(dA.getter_shape()[0] != this.l_1)
		// {
		// 	throw new IllegalArgumentException("illegal input data size");
		// }

		// this.W.repr(true);
		// this.b.repr(true);
		// dA.repr();
		// this.activation.cached.repr();
		NDArray dZ = this.activation.backward(dA);
		// System.out.print("*********");
		// dZ.repr(true);
		// this.cached.repr(true);
		NDArray dW = dZ.dot(this.cached.T()).dot(1.0/m);
		// dW.repr();
		// dW.repr(true);
		NDArray db = dZ.sum(1).dot(1.0/m);
		// db.repr();
		// db.repr(true);
		NDArray _dA = dW.T().dot(dZ);
		// _dA.repr(true);

		this.W = this.W.sub(dW.dot(this.learning_rate));
		this.b = this.b.sub(db.dot(this.learning_rate));
		return _dA;
	}

	// public static void backward(NDArray )

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

		int[] dims1 = {2,3};
		int[] dims2 = {2,3};
		int[] dims3 = {2,6};
		NDArray ndarr1 = new NDArray(arr1d, dims1, 'C');
		NDArray ndarr2 = new NDArray(arr2d, dims2, 'C');
		NDArray ndarr3 = new NDArray(arr3d, dims3, 'C');
		// Long i = ndarr.idx(1,2);
		// System.out.println(ndarr.dtype.NAME);
		// System.out.println(i);
		DType dtype = new DType(new Int32());
		// NDArray ndarr_self = new NDArray(dims1, dtype, 'C');
		// // System.out.println(ndarr_self.idx(1,1));
		// // System.out.println(ndarr_self.dtype.NAME);
		// NDArray zeros = NDArray.zeros(dims1, dtype, 'C');
		// NDArray ones = NDArray.ones(dims1, dtype, 'C');
		// zeros.repr();
		// ones.repr();
		// ndarr1.repr();
		// ndarr2.repr();
		// ndarr3.repr();
		// // ndarr_self.repr();
		// int[] newdims = {12};
		// ndarr3.reshape(newdims);
		// ndarr3.repr();
		// int [] onedim = {10};
		// NDArray oned = new NDArray(onedim, dtype, 'C');
		// oned.repr();
		// int[] newnewdims = {2,6};
		// NDArray ndarr4 = NDArray.reshape(ndarr3, newnewdims);
		// ndarr4.repr();
		// ndarr1.dot(0.33).repr(true);

		DenseLayer l0 = new DenseLayer(dims3, "Sigmod");
		l0.W.repr();
		l0.b.repr();
		NDArray A = l0.forward(ndarr1);
		l0.cached.repr();
		// A.repr();
		// l0.W.repr(true);
		// l0.b.repr();
		NDArray dA = A.add(10);
		// dA.repr();
		NDArray _dA = l0.backward(dA);
		// l0.W.repr(true);
		// _dA.repr();
		// l0.W.repr();
		// l0.b.repr();


		// l0.backward(ndarr2);

	}
}