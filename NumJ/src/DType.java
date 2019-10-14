/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
import java.nio.ByteBuffer;

public class DType
{
	public String NAME;
	public int itemsize;
	public Character byte_order;
	public BaseType btype;

	public DType(BaseType btype)
	{
		this.NAME = btype.getNAME();
		this.itemsize = btype.getitemsize();
		this.btype = btype;
	}


	public <N extends Number> N parseByte(byte[] input, int index)
	{
		switch(this.btype.getClass().getName())
		{
			case "Int64":
			{
				byte[] bytes = new byte[8];
				System.arraycopy(input, index, bytes, 0, 8); 
				return (N) new Long(ByteBuffer.wrap(bytes).getLong());
			}
			case "Int16":
			{
				byte[] bytes = new byte[2];
				System.arraycopy(input, index, bytes, 0, 2); 
				return (N) new Short(ByteBuffer.wrap(bytes).getShort());
			}
			case "Int32":
			{
				byte[] bytes = new byte[4];
				System.arraycopy(input, index, bytes, 0, 4); 
				return (N) new Integer(ByteBuffer.wrap(bytes).getInt());
			}
			case "Float32":
			{
				byte[] bytes = new byte[4];
				System.arraycopy(input, index, bytes, 0, 4); 
				return (N) new Float(ByteBuffer.wrap(bytes).getFloat()) ;
			}
			case "Float64":
			{
				byte[] bytes = new byte[8];
				System.arraycopy(input, index, bytes, 0, 8); 
				return (N) new Double(ByteBuffer.wrap(bytes).getDouble());
			}
			default: return null;
		}
	}

	public <N extends Number> byte[] toByte(N value)
	{
		switch(this.btype.getClass().getName())
		{
			case "Int64":
			{
				return Utils.LONG_2_BYTE((Long)value);
			}
			case "Int16":
			{
				return Utils.SHORT_2_BYTE((Short)value);
			}
			case "Int32":
			{
				return Utils.INT_2_BYTE((Integer)value);
			}
			case "Float32":
			{
				return Utils.FLOAT_2_BYTE((Float)value);
			}
			case "Float64":
			{
				return Utils.DOUBLE_2_BYTE((Double)value);
			}
			default: 
				throw new IllegalArgumentException("bad input Array type"); // actually the wrong
		}
	}

	public static void main(String[] arv)
	{
		Float64 type = new Float64();
		DType test = new DType(type);
		System.out.println(test.NAME);
		System.out.println(test.itemsize);
		System.out.println(test.btype.getClass().getName());

		Double l = 1231248723478.12;
		byte[] r = test.toByte(l);
		Utils.reprBytes(r,r.length);
		Double rr = test.parseByte(r, 0);
		System.out.println(rr);

	}
}

	// public static double fromByte(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7)
	// {
	// 	byte[] bytes = {b0, b1, b2, b3, b4, b5, b6, b7};
	// 	return ByteBuffer.wrap(bytes).getDouble();
	// }

	// public static byte[] toByte(Double value)
	// {
	// 	return Utils.DOUBLE_2_BYTE(value);
	// }

	// public static float FromByte(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7)
	// {
	// 	byte[] bytes = {b0, b1, b2, b3, b4, b5, b6, b7};
	// 	return ByteBuffer.wrap(bytes).getLong();
	// }

	// public static byte[] toByte(Long value)
	// {
	// 	return Utils.LONG_2_BYTE(value);
	// }

	// public static short fromByte(byte b0, byte b1)
	// {
	// 	byte[] bytes = {b0, b1};
	// 	return ByteBuffer.wrap(bytes).getShort();
	// }

	// public static byte[] toByte(Short value)
	// {
	// 	return Utils.SHORT_2_BYTE(value);
	// }


	// public static float fromByte(byte b0, byte b1, byte b2, byte b3)
	// {
	// 	byte[] bytes = {b0, b1, b2, b3};
	// 	return ByteBuffer.wrap(bytes).getFloat();
	// }

	// public static byte[] toByte(Float value)
	// {
	// 	return Utils.FLOAT_2_BYTE(value);
	// }


	// public static int fromByte(byte b0, byte b1, byte b2, byte b3)
	// {
	// 	return b3 + (b2<<8) + (b1<<16) + (b0<<24);
	// }
	
	// public static byte[] toByte(Integer value)
	// {
	// 	return Utils.INT_2_BYTE(value);
	// }