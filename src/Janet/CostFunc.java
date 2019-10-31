package Janet;

import NumJ.core.*;
import NumJ.type.*;

public class CostFunc
{
	NDArray error;
	String func_type;
	CostFunc(String cost_func)
	{
		switch(cost_func)
		{
			case "Logistic": break;

			default:
				throw new IllegalArgumentException("unsupported cost type");
		}
		this.func_type = cost_func;
	}
	public NDArray logistic(NDArray A, NDArray Y)
	{
		//L(ŷ ,y)=−(ylogŷ )−(1−y)log(1−ŷ )
		NDArray _A = A.dot(-1).add(1);
		NDArray _Y = Y.dot(-1).add(1);
		NDArray err = A.multiply(Y.log()).dot(-1).sub(_A.multiply(_Y.log()));
		this.error = err.sum(1);
		NDArray dA = _Y.divide(_A).sub(Y.divide(A));
		return dA;
	}
	public NDArray compute_cost(NDArray A, NDArray Y)
	{
		switch(this.func_type)
		{
			case "Logistic":
			{
				return logistic(A, Y);
			}
			default:
				throw new IllegalArgumentException("__");	
		}
	}
	public static void main(String [] args)
	{
		
	}
}