/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
package NumJ.type;

public class Int64 implements BaseType // long
{
	final static public String NAME = "NumJ.Int64";
	final static public int itemsize = 8; // byte size
	public String getNAME()
	{
		return this.NAME;
	}
	public int getitemsize()
	{
		return this.itemsize;
	}
}