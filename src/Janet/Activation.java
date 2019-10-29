package Janet;

import NumJ.core.*;
import NumJ.type.*;
import NumJ.math.*;

// basically all non-linear function
// the reason why we call it pseudo is because we don't really
// use them as the operation node in tensorflow, but we do have
// similar mindset.

// Activation are using the "subgraph" design pattern,
// which can be used to design any-nonlinear function and 
// sub-computer graph
public class Activation
{
	// public static relu(int dir, NDArray input, NDArray output)
	// {
	// 	switch(dir)
	// 	{
	// 		case 1: // forward
	// 		{
					
	// 		}

	// 		case 0: // back propagation
	// 		{

	// 		}
	// 	}
	// }
	// public static leakyRelu(int dir, NDArray input, NDArray output)
	// {

	// }

	// in-place ops, as we can always pass a copy as we will
	// return the cached value for this node
	public static NDArray sigmod_forward(NDArray input)
	{
		if(!input.getter_dtype().NAME.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("double plz!!");
		}
		NDArray cached_input = NDArray.deepCopy(input);
		MyFunc sigmod = (bytes) ->{
			double val = DType.parseByteDouble(bytes, 0);
			return DType.toByteAuto(1.0 / (1.0 + Math.exp(-val)));
		};
		input.each_do(sigmod);
		return cached_input;
	}
	public static void sigmod_backward(NDArray input_d, NDArray cached_input)
	{
		if(!input_d.getter_dtype().NAME.equals("NumJ.Float64") || !cached_input.getter_dtype().NAME.equals("NumJ.Float64"))
		{
			throw new IllegalArgumentException("double plz!!");
		}
		MyFunc d_sigmod = (bytes) ->{
			double val = DType.parseByteDouble(bytes, 0);
			double e_x = Math.exp(-val);
			return DType.toByteAuto(e_x/Math.pow(1+e_x, 2));
		};
		cached_input.each_do(d_sigmod);
		input_d.multiple(cached_input);
	}

	public static void main(String[] args)
	{

	}
}