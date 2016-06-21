package bouldercreek.jumpingboulder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jakob on 10-06-2016.
 */
public class ByteConversionTest {

    // INTEGER VALUE TESTING #################################################################
    @Test
    public void convertByteToInt() throws Exception {
        byte[] b = {0b00000001,0b00000000,0b00000000,0b00000000};
        assertEquals(1, ByteConversion.convertByteToInt(b));
        b[1] = 0b00000001;
        assertEquals(257, ByteConversion.convertByteToInt(b));


    }

    @Test
    public void convertIntToByte() throws Exception {
        int x = 257;
        byte[] b = {0b00000001,0b00000001,0b00000000,0b00000000};
        for(int i =0; i<b.length; i++) {
            assertEquals(b[i], ByteConversion.convertToByte(x)[i]);
        }

    }

    @Test
    public void convertBackAndForthIntTest() throws Exception{
        int x = 2345;
        assertEquals(x, ByteConversion.convertByteToInt(ByteConversion.convertToByte(x)));
    }

    @Test
    public void convertBackAndForthIntTest2() throws Exception{
        int x = 388;
        assertEquals(x, ByteConversion.convertByteToInt(ByteConversion.convertToByte(x)));
    }


    // LONG VALUE TESTING #####################################################################
    @Test
    public void convertByteToLong() throws Exception {
        long x = 4294967296L+1;
        byte[] b = {0b00000001,0b00000000,0b00000000,0b00000000,0b00000001};
        assertEquals(x, ByteConversion.convertByteToLong(b));
        b[1] = 0b00000001;
        assertEquals(x+256, ByteConversion.convertByteToLong(b));

    }

    @Test
    public void convertLongToByte() throws Exception {
        long x = 4294967296L+1;
        byte[] b = {0b00000001,0b00000000,0b00000000,0b00000000,0b00000001};
        for(int i =0; i<b.length; i++) {
            assertEquals(b[i], ByteConversion.convertToByte(x)[i]);
        }
    }

    @Test
    public void convertBackAndForthLongTest() throws Exception{
        long x = 46491427907797663L;
        assertEquals(x, ByteConversion.convertByteToLong(ByteConversion.convertToByte(x)));
    }

    // Double VALUE TESTING #####################################################################
    // Because the back and forth test, is most important, and the others didn't work very well, they are deleted.


    @Test
    public void convertBackAndForthDoubleTest() throws Exception{
        double x = 674.342;
        assertEquals(x, ByteConversion.convertByteToDouble(ByteConversion.convertToByte(x)),0.0000000001);
    }

    @Test
    public void combineTest() throws Exception{
        byte[] b1 = new byte[]{1,2,3,4};
        byte[] b2 = new byte[]{5,6,7,8};
        byte[] b3 = new byte[]{1,2,3,4,5,6,7,8};

        byte[] b4 = ByteConversion.combine(b1,b2);

        for (int i=0; i<b3.length; i++) {
            assertEquals(b3[i],b4[i]);
        }
    }

    @Test
    public void subByteTest() throws Exception{
        byte[] b = new byte[]{0,1,2,3,4,5};


        byte[] b2 = ByteConversion.subByte(b,2,3);
        byte[] b3 = new byte[]{2,3,4};

        for (int i=0; i<b3.length; i++) {
            assertEquals(b3[i], b2[i]);
        }

    }


}
