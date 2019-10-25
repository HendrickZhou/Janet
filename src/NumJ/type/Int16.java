/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
package NumJ.type;

public class Int16 implements BaseType // short
{
	final static public String NAME = "NumJ.Int16";
	final static public int itemsize = 2; // byte size
	public String getNAME()
	{
		return this.NAME;
	}
	public int getitemsize()
	{
		return this.itemsize;
	}
}