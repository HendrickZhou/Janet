/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/

class Float64 implements BaseType // double
{
	final static public String NAME = "NumJ.Float64";
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