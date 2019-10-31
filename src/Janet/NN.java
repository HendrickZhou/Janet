package Janet;

import NumJ.core.*;
import NumJ.type.*;
import java.util.Arrays;

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
		layers[depth-1] = new DenseLayer(units[depth-1], units[depth-1], activation, this.learning_rate);
		for(int i = depth-2; i >=0; i--)
		{
			layers[i] = new DenseLayer(units[i], units[i+1], activation, this.learning_rate);
		}
		this.cost_func = new CostFunc(cost_func);

		// for(int i = 0; i < depth; i++)
		// {
		// 	System.out.println(layers[i].l_1);
		// 	System.out.println(layers[i].l_1);
		// 	System.out.println("*********");
		// }
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
		// out.show_shape();
		for(int j = 0; j < this.depth; j++)
		{
			out = this.layers[j].forward(out);
			// out.show_shape();
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

	public void show_W_b()
	{
		for(int i = 0; i < this.depth; i++)
		{
			this.layers[i].W.repr();
			this.layers[i].b.repr();
			System.out.println(String.format("layers: %s", i));
		}
	}
	public void train(NDArray X, NDArray Y, int batch_size, int epochs)
	{
		// Y has to align with X
		// X has to be 2d matrix
		int dataset_size = X.getter_shape()[1];
		int dataset_dim = X.getter_shape()[0];
		int batch_num = dataset_size /batch_size;
		for(int e = 0; e < epochs; e++)
		{
			for(int i = 0; i < batch_num; i++)
			{
				NDArray batch = X.slc_2d_col(i*batch_size, (i+1)*batch_size);
				batch.repr();
				NDArray batch_label = Y.slc_2d_col(i*batch_size, (i+1)*batch_size);
				batch_label.repr();
				this.train_on_single(batch, batch_label);
				this.predict(batch).repr();
			}
			if(dataset_size % batch_size != 0)
			{			
				NDArray tail_batch = X.slc_2d_col(batch_size*batch_num, dataset_size);
				// tail_batch.repr();
				NDArray tail_label = Y.slc_2d_col(batch_size*batch_num, dataset_size);
				// tail_label.repr();
				train_on_single(tail_batch, tail_label);
				this.predict(tail_batch).repr();
			}
		}
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
		// data.repr();
		// label_and.repr();
		// label_or.repr();

		int[] units = {2, 2, 1};
		NN Lucy = new NN(units, "Sigmod", "Logistic");
		System.out.println("*******Initliazed*******");
		Lucy.show_W_b();
		System.out.println("**************");
		// Lucy.predict(data.slc_2d_col(0,1)).repr();
		for(int i = 0; i < 1; i++)
		{
			Lucy.train_on_single(data.slc_2d_col(3,4), label_and.slc_2d_col(3, 4));	
			System.out.println(String.format("loop: %d", i));
			Lucy.show_W_b();
		}	
		Lucy.predict(data.slc_2d_col(3,4)).repr();

		// Lucy.train(data, label_and, 4, 10);
		// Lucy.predict(data.slc_2d_col(0,1)).repr();
	}
}