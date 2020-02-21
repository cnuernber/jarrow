package jarrow.feather;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;

public class BufUtils {

    public static final Charset UTF8 = Charset.forName( "UTF-8" );

    private BufUtils() {
    }

    public static int longToInt( long index ) {
        int ix = (int) index;
        if ( ix == index ) {
            return ix;
        }
        else {
            throw new RuntimeException( "Integer overflow!" );
        }
    }

    public static int readLittleEndianInt( RandomAccessFile raf )
            throws IOException {
        return ( raf.read() & 0xff ) <<  0
             | ( raf.read() & 0xff ) <<  8
             | ( raf.read() & 0xff ) << 16
             | ( raf.read() & 0xff ) << 24;
    }


    public static void writeLittleEndianLong( OutputStream out, long l )
            throws IOException {
        out.write( ( (int) ( l >>  0 ) ) & 0xff );
        out.write( ( (int) ( l >>  8 ) ) & 0xff );
        out.write( ( (int) ( l >> 16 ) ) & 0xff );
        out.write( ( (int) ( l >> 24 ) ) & 0xff );
        out.write( ( (int) ( l >> 32 ) ) & 0xff );
        out.write( ( (int) ( l >> 40 ) ) & 0xff );
        out.write( ( (int) ( l >> 48 ) ) & 0xff );
        out.write( ( (int) ( l >> 56 ) ) & 0xff );
    }

    public static void writeLittleEndianInt( OutputStream out, int i )
            throws IOException {
        out.write( ( i >>  0 ) & 0xff );
        out.write( ( i >>  8 ) & 0xff );
        out.write( ( i >> 16 ) & 0xff );
        out.write( ( i >> 24 ) & 0xff );
    }

    public static void writeLittleEndianShort( OutputStream out, short s )
            throws IOException {
        out.write( ( s >> 0 ) & 0xff );
        out.write( ( s >> 8 ) & 0xff );
    }

    public static void writeLittleEndianDouble( OutputStream out, double d )
            throws IOException {
        writeLittleEndianLong( out, Double.doubleToLongBits( d ) );
    }

    public static void writeLittleEndianFloat( OutputStream out, float f )
            throws IOException {
        writeLittleEndianInt( out, Float.floatToIntBits( f ) );
    }

    public static int align8( OutputStream out, long nb ) throws IOException {
        int over = (int) ( nb % 8 );
        int pad;
        if ( over > 0 ) {
            pad = 8 - over;
            for ( int i = 0; i < pad; i++ ) {
                out.write( 0 );
            }
        }
        else {
            pad = 0;
        }
        return pad;
    }

    /**
     * @param  nb  positive
     */
    public static long ceil8( long nb ) {
        return ( ( nb + 7 ) / 8 ) * 8;
    }

    public static byte[] marker8( byte value ) {
        byte[] buf = new byte[ 8 ];
        Arrays.fill( buf, value );
        return buf;
    }

    public static int utf8Length( String txt ) {

        // copied from https://stackoverflow.com/questions/8511490
        int count = 0;
        int nc = txt.length();
        for ( int i = 0; i < nc; i++ ) {
            char ch = txt.charAt( i );
            if ( ch <= 0x7F ) {
                count++;
            }
            else if ( ch <= 0x7FF ) {
                count += 2;
            }
            else if ( Character.isHighSurrogate( ch ) ) {
                count += 4;
                ++i;
            }
            else {
                count += 3;
            }
        }
        assert count == txt.getBytes( UTF8 ).length;
        return count;
    }
}
