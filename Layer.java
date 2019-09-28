import NumJ
public class Layer
{
	public NumJ w;
	public NumJ b;
	public static NumJ Relu();
	public static NumJ Sigmod();
	public static NumJ activation(String type)
	{
		switch(type)
		{
			case "Relu":
				return Relu();
			case "Sigmod":
				return Sigmod();
			default:
				return None;
		}
	}

	public static forward(NumJ x_i, String type)
	{
		NumJ y_i = this.activation(NumJ.add(NumJ.multiple(this.w, x_i), this.b), type = "Relu");
		return y_i;
	}

	public static backward()
}