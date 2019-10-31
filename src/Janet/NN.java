package Janet;

import NumJ.core.*;
import NumJ.type.*;

public class NN
{
	private DenseLayer[] layers;
	private int depth;
	private CostFunc cost_func;
	private double learning_rate;

	public NN() { };

	public NN(int[] units, String activation, String cost_func)
	{
		this.depth = units.length;
		this.layers = new DenseLayer[depth];
		this.learning_rate = 0.1;
		layers[depth-1] = new DenseLayer(units[depth], units[depth], activation, this.learning_rate);
		for(int i = depth-2; i >=0; i--)
		{
			layers[i] = new DenseLayer(units[i+1], units[i], activation, this.learning_rate);
		}
		this.cost_func = new CostFunc(cost_func);
	}
	// public void add_layer()
	// {

	// }

	private NDArray forward_NN(NDArray X)
	{
		int data_dims = X.getter_shape()[0];
		int batch_size = X.getter_shape()[1];

		for(int i = 0; i < depth; i++)
		{
			layers[i].set_m(batch_size);
		}
		NDArray out = NDArray.deepCopy(X);
		for(int j = 0; j < this.depth; j++)
		{
			out = this.layers[j].forward(out);
		}
		return out;
	}

	private void train_on_single(NDArray X, NDArray Y)
	{
		NDArray deriv = this.cost_func.compute_cost(this.forward_NN(X), Y);
		for(int j = this.depth - 1; j >= 0; j--)
		{
			deriv = this.layers[j].backward(deriv);
		}
	}

	public void train(NDArray X, NDArray Y, int batch_size, int epochs)
	{
		
	}

	public NDArray predict(NDArray X)
	{
		return this.forward_NN(X);
	}


	public static void main(String [] args)
	{
		Integer[][] _data = {{0,0},{0,1}, {1,0}, {1,1}};
		Integer[][] _label_and = {{0}, {0}, {0}, {1}};
		Integer[][] _label_or = {{0}, {1}, {1}, {1}};
		int[] shape_data = {4, 2};
		int[] shape_and = {4, 1};
		int[] shape_or = {4, 1};
		NDArray data = new NDArray(_data, shape_data, null);
		NDArray label_and = new NDArray(_label_and, shape_and, null);
		NDArray label_or = new NDArray(_label_or, shape_or, null);
		// int[] input_shape = {2,4};
		// int[] output_shape = {1,4};
		// data.reshape(input_shape);
		// label_and.reshape(output_shape);
		// label_or.reshape(output_shape);
		// train_on_single()
		data = data.T();
		label_and = label_and.T();
		label_or = label_or.T();
		data.repr();
		label_or.repr();
		label_or.repr();

	}
}