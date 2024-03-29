/*
* @Author: 	Hang Zhou
* @Email:	zhouhangseu@gmail.com
*/
package NumJ.core;

import NumJ.type.*;

import java.nio.ByteBuffer;

public class DType
{
	public String NAME;
	public int itemsize;
	public BaseType btype;

	// should use setter and getter for this

	public DType() { }

	public DType(BaseType btype)
	{
		this.NAME = btype.getNAME();
		this.itemsize = btype.getitemsize();
		this.btype = btype;
	}
	protected boolean isNull()
	{
		return NAME == null || btype == null;
	}

	public static int valueOf(Integer value)
	{
		return value.intValue();
	}
	public static double valueOf(Double value)
	{
		return value.doubleValue();
	}
	public static short valueOf(Short value)
	{
		return value.shortValue();
	}
	public static long valueOf(Long value)
	{
		return value.longValue();
	}
	public static float valueOf(Float value)
	{
		return value.floatValue();
	}

	public static int parseByteInt(byte[] input, int index)
	{
		byte[] bytes = new byte[4];
		System.arraycopy(input, index, bytes, 0, 4); 
		return ByteBuffer.wrap(bytes).getInt();	
	}
	public static long parseByteLong(byte[] input, int index)
	{
		byte[] bytes = new byte[8];
		System.arraycopy(input, index, bytes, 0, 4); 
		return ByteBuffer.wrap(bytes).getLong();	
	}
	public static short parseByteShort(byte[] input, int index)
	{
		byte[] bytes = new byte[2];
		System.arraycopy(input, index, bytes, 0, 4); 
		return ByteBuffer.wrap(bytes).getShort();	
	}
	public static float parseByteFloat(byte[] input, int index)
	{
		byte[] bytes = new byte[4];
		System.arraycopy(input, index, bytes, 0, 4); 
		return ByteBuffer.wrap(bytes).getFloat();	
	}
	public static double parseByteDouble(byte[] input, int index)
	{
		byte[] bytes = new byte[8];
		System.arraycopy(input, index, bytes, 0, 4); 
		return ByteBuffer.wrap(bytes).getDouble();	
	}

	public <N extends Number> N parseByte(byte[] input, int index)
	{
		switch(this.btype.getClass().getSimpleName())
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

	/**
	* Only care about converting corresponding Number object to byte
	* Type casting not implemented here
	* if the wrong Number object is passed, it will throw an exception
	* The Autobox works here!!
	*/
	public <N extends Number> byte[] toByte(N value)
	{
		switch(this.btype.getClass().getSimpleName())
		{
			case "Int64":
			{
				if (!value.getClass().getSimpleName().equals("Long"))
				{
					throw new IllegalArgumentException("input type not Long");
				}
				return Utils.LONG_2_BYTE((Long)value);
			}
			case "Int16":
			{
				if (!value.getClass().getSimpleName().equals("Short"))
				{
					throw new IllegalArgumentException("input type not Short");
				}
				return Utils.SHORT_2_BYTE((Short)value);
			}
			case "Int32":
			{
				if (!value.getClass().getSimpleName().equals("Integer"))
				{
					throw new IllegalArgumentException("input type not Integer");
				}
				return Utils.INT_2_BYTE((Integer)value);
			}
			case "Float32":
			{
				if (!value.getClass().getSimpleName().equals("Float"))
				{
					throw new IllegalArgumentException("input type not Float");
				}
				return Utils.FLOAT_2_BYTE((Float)value);
			}
			case "Float64":
			{
				if (!value.getClass().getSimpleName().equals("Double"))
				{
					throw new IllegalArgumentException("input type not Double");
				}
				return Utils.DOUBLE_2_BYTE((Double)value);
			}
			default: 
				throw new IllegalArgumentException("bad input Array type"); // actually the wrong
		}
	}
	// utils function for converting value to bytes array, without checking the conflicts
	public static <N extends Number> byte[] toByteAuto(N value)
	{
		switch(value.getClass().getSimpleName())
		{
			case "Int":
			{
				return Utils.INT_2_BYTE((Integer)value);
			}
			case "Double":
			{
				return Utils.DOUBLE_2_BYTE((Double)value);
			}
			case "Long":
			{
				return Utils.LONG_2_BYTE((Long)value);
			}
			case "Short":
			{
				return Utils.SHORT_2_BYTE((Short)value);
			}
			case "Float":
			{
				return Utils.FLOAT_2_BYTE((Float)value);
			}
			default:
				throw new IllegalArgumentException("unsupported Number type"); 									
		}
	}

	public static void main(String[] arv)
	{
		// Float64 type = new Float64();
		// DType test = new DType(type);
		// System.out.println(test.NAME);
		// System.out.println(test.itemsize);
		// System.out.println(test.btype.getClass().getName());

		// Double l = 1231248723478.12;
		// byte[] r = test.toByte(l);
		// Utils.reprBytes(r,r.length);
		// Double rr = test.parseByte(r, 0);
		// System.out.println(rr);
		DType test= new DType();
		if(test.isNull())
		{
			System.out.println("null");
		}

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