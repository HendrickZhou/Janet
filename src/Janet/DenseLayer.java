package Janet;

import NumJ.core.*;
import NumJ.type.*;

public class DenseLayer
{
	// public NumJ w;
	// public NumJ b;
	// public static NumJ Relu();
	// public static NumJ Sigmod();
	// public static NumJ activation(String type)
	// {
	// 	switch(type)
	// 	{
	// 		case "Relu":
	// 			return Relu();
	// 		case "Sigmod":
	// 			return Sigmod();
	// 		default:
	// 			return None;
	// 	}
	// }

	// public static forward(NumJ x_i, String type)
	// {
	// 	NumJ y_i = this.activation(NumJ.add(NumJ.multiple(this.w, x_i), this.b), type = "Relu");
	// 	return y_i;
	// }

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