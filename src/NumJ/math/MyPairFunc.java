package NumJ.math;

/**
* User is responsiable to interprete the byte data.!!!!!
* make sure the result will be returned as 8 bytes array!!!!!
*/
@FunctionalInterface
public interface MyPairFunc{
	byte[] doMath(byte[] data1, byte[] data2);
}