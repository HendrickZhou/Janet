/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/

class Float32 implements BaseType // float
{
	final static public String NAME = "NumJ.Float32";
	final static public int itemsize = 4; // byte size
	public String getNAME()
	{
		return this.NAME;
	}
	public int getitemsize()
	{
		return this.itemsize;
	}
}