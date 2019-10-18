/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
// package org.NumJ.core;

class Matrix
{
	// we do not handle uncertain dimension array here
	// Since the type conversion haven't been finished yet, all the operation are assumed
	// to be the same data type
	// but since we're using the java's default number operation type casting, we'll not check the 
	// type correctness here as this part actually has some flexibility in type
	protected static <N extends Number> NDArray scalar(NDArray ndarr, N scalar, broadcast)
	{
		// should check type

		// switch(scalar.getClass().getSimpleName())
		// {
		// 	case "Integer":
		// 	{
		// 		DType.valueof()
		// 	}

		// 	case "Short":
		// 	{

		// 	}

		// 	case "Long":
		// 	{

		// 	}

		// 	case "Float":
		// 	{

		// 	}

		// 	case "Double":
		// 	{

		// 	}
		// }
		for(int i = 0; i < this.)
	}
	protected static NDArray innerProduct();
	protected static NDArray dotProduct();
	protected static NDArray mmul();
	protected static void main(String[] args)
	{

	}
}