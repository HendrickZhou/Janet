package Janet;

import NumJ.core.*;
import NumJ.type.*;

public class CostFunc
{
	NDArray error;
	CostFunc() { }
	public static NDArray logistic(NDArray A, NDArray Y)
	{
		//L(ŷ ,y)=−(ylogŷ )−(1−y)log(1−ŷ )
		NDArray _A = A.dot(-1).add(1);
		NDArray _Y = Y.dot(-1).add(1);
		NDArray err = A.multiply(A, Y.log()).dot(-1).sub(_A.multiply(_Y.log()));
		this.error = err.sum(1);
		NDArray dA = _Y.divide(_A).sub(Y.divide(A));
		return dA;
	}
}