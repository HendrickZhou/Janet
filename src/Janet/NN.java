import Layer
import NumJ

public class NN
{
	private Array layers;

	private NumJ cache; // cache from computing the forward procedure

	public static NN();

	public static add_layer();
	public static 
	public static train(X, Y, learning_rate, iteration, verbose)
	{
		for(int i = 0; i < iteration; i++)
		{
			forward();
			backpro();
		}
	}

	private static forward(NumJ x_i)
	{
		NumJ h_i = x_i;
		for(Layer layer : this.layers)
		{
			h_i = layer.forward(h_i);
		}
		return h_i
	}
	private static backpro();
	{
		
	}
}