package bouldercreek.jumpingboulder;

/**
 * Created by jakob on 13-06-2016.
 */
public class ByteConversion {

    //METHODS TO CONVERT TYPES TO BYTE ARRAYS  #######################################
    public static byte[] convertToByte(int x){
        byte[] b = new byte[4];
        for(int i=0; i<b.length; i++){
            b[i] = (byte) (x>>(i*8));
        }

        return b;
    }

    public static byte[] convertToByte(long x){
        byte[] b = new byte[8];
        for(int i=0; i<b.length; i++){
            b[i] = (byte) (x>>(i*8));
        }

        return b;
    }


    public static byte[] convertToByte(Double d){
        long l =  Double.doubleToRawLongBits(d);
        return convertToByte(l);
    }


    //METHODS TO CONVERT BYTE ARRAYS TO OTHER NUMBER TYPES #######################
    public static int convertByteToInt(byte[] b){
        testLength(b,"int", 4);
        int x = b[0];
        for(int i=1; i<b.length; i++){
            x += b[i]<<(8*i);
        }

        return x;
    }

    public static long convertByteToLong(byte[] b){
        testLength(b,"long", 8);
        long sum = 0;

        for(int i=0; i<b.length; i++){
            sum += (((long) b[i])&0b11111111)<<(8*i);
        }

        return sum;
    }

    public static double convertByteToDouble(byte[] b){
        testLength(b, "double", 8);
        long l = convertByteToLong(b);
        return Double.longBitsToDouble(l);
    }

    //HELPER METHODS #########################################
    private static void testLength(byte[] b,String s, int l){
        if(b.length > l){
            throw new NumberFormatException("Tried to convert " + printBytes(b) + " to "+s+", but the byte array was too big");
        }
    }

    public static String printBytes(byte[] b){
        String byteString = ""+b[0];
        for(int i=1; i<b.length; i++){
            byteString += " : "+b[i];
        }
        return byteString;
    }

}
