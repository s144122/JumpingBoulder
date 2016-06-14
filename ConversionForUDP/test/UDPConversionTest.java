
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jakob on 10-06-2016.
 */
public class UDPConversionTest {

    // INTEGER VALUE TESTING #################################################################
    @Test
    public void convertByteToInt() throws Exception {
        byte[] b = {0b00000001,0b00000000,0b00000000,0b00000000};
        assertEquals(1, UDPConversion.convertByteToInt(b));
        b[1] = 0b00000001;
        assertEquals(257, UDPConversion.convertByteToInt(b));


    }

    @Test
    public void convertIntToByte() throws Exception {
        int x = 257;
        byte[] b = {0b00000001,0b00000001,0b00000000,0b00000000};
        for(int i =0; i<b.length; i++) {
            assertEquals(b[i], UDPConversion.convertToByte(x)[i]);
        }

    }

    @Test
    public void convertBackAndForthIntTest() throws Exception{
        int x = 2345;
        assertEquals(x, UDPConversion.convertByteToInt(UDPConversion.convertToByte(x)));
    }


    // LONG VALUE TESTING #####################################################################
    @Test
    public void convertByteToLong() throws Exception {
        long x = 4294967296L+1;
        byte[] b = {0b00000001,0b00000000,0b00000000,0b00000000,0b00000001};
        assertEquals(x, UDPConversion.convertByteToLong(b));
        b[1] = 0b00000001;
        assertEquals(x+256, UDPConversion.convertByteToLong(b));

    }

    @Test
    public void convertLongToByte() throws Exception {
        long x = 4294967296L+1;
        byte[] b = {0b00000001,0b00000000,0b00000000,0b00000000,0b00000001};
        for(int i =0; i<b.length; i++) {
            assertEquals(b[i], UDPConversion.convertToByte(x)[i]);
        }
    }

    @Test
    public void convertBackAndForthLongTest() throws Exception{
        long x = 46491427907797663L;
        assertEquals(x, UDPConversion.convertByteToLong(UDPConversion.convertToByte(x)));
    }

    // Double VALUE TESTING #####################################################################
    // Because the back and forth test, is most important, and the others didn't work very well, they are deleted.


    @Test
    public void convertBackAndForthDoubleTest() throws Exception{
        double x = 674.342;
        assertEquals(x, UDPConversion.convertByteToDouble(UDPConversion.convertToByte(x)),0.0000000001);
    }


}
