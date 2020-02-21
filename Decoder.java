package jarrow.feather;

import jarrow.fbs.Type;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

@SuppressWarnings("cast")
public abstract class Decoder<T> {

    private final Class<T> clazz_;
    private final String name_;

    private static final Logger logger_ =
        Logger.getLogger( Decoder.class.getName() );

    private Decoder( Class<T> clazz, String name ) {
        clazz_ = clazz;
        name_ = name;
    }

    public abstract Reader<T> createReader( ByteBuffer buf, long nrow );

    public Class<T> getValueClass() {
        return clazz_;
    }

    @Override
    public String toString() {
        return name_;
    }

    private static final Decoder<?>[] TYPE_DECODERS = createTypeDecoders();

    public static Decoder<?> createDecoder( byte type ) {
        Decoder<?> decoder = type >= 0 && type <= TYPE_DECODERS.length
                           ? TYPE_DECODERS[ type ]
                           : null;
        if ( decoder != null ) {
            return decoder;
        }
        else {
            logger_.warning( "No decoder for data type " + type );
            return new UnsupportedDecoder( "???" + type + "???" );
        }
    }

    private static Decoder<?>[] createTypeDecoders() {
        Decoder<?>[] decoders = new Decoder<?>[ 19 ];
        decoders[ Type.BOOL ] = new Decoder<Boolean>( Boolean.class, "BOOL" ) {
            public Reader<Boolean> createReader( final ByteBuffer bbuf,
                                                 long nrow ) {
                return new AbstractReader<Boolean>( Boolean.class ) {
                    private boolean get( long ix ) {
                        return BufUtils.isBitSet( bbuf, ix );
                    }
                    public Boolean getObject( long ix ) {
                        return Boolean.valueOf( get( ix ) );
                    }
                    public byte getByte( long ix ) {
                        return get( ix ) ? (byte) 1 : (byte) 0;
                    }
                    public short getShort( long ix ) {
                        return get( ix ) ? (short) 1 : (short) 0;
                    }
                    public int getInt( long ix ) {
                        return get( ix ) ? 1 : 0;
                    }
                    public long getLong( long ix ) {
                        return get( ix ) ? 1L : 0L;
                    }
                    public float getFloat( long ix ) {
                        return get( ix ) ? 1f : 0f;
                    }
                    public double getDouble( long ix ) {
                        return get( ix ) ? 1. : 0.;
                    }
                };
            };
        };
        decoders[ Type.INT8 ] = new Decoder<Byte>( Byte.class, "INT8" ) {
            public Reader<Byte> createReader( final ByteBuffer bbuf,
                                              long nrow ) {
                return new AbstractReader<Byte>( Byte.class ) {
                    private byte get( long ix ) {
                        return bbuf.get( BufUtils.longToInt( ix ) );
                    }
                    public Byte getObject( long ix ) {
                        return Byte.valueOf( get( ix ) );
                    }
                    public byte getByte( long ix ) {
                        return (byte) get( ix );
                    }
                    public short getShort( long ix ) {
                        return (short) get( ix );
                    }
                    public int getInt( long ix ) {
                        return (int) get( ix );
                    }
                    public long getLong( long ix ) {
                        return (long) get( ix );
                    }
                    public float getFloat( long ix ) {
                        return (float) get( ix );
                    }
                    public double getDouble( long ix ) {
                        return (double) get( ix );
                    }
                };
            }
        };
        decoders[ Type.INT16 ] = new Decoder<Short>( Short.class, "INT16" ) {
            public Reader<Short> createReader( ByteBuffer bbuf, long nrow ) {
                final ShortBuffer sbuf = bbuf.asShortBuffer();
                return new ShortReader() {
                    short get( long ix ) {
                        return sbuf.get( BufUtils.longToInt( ix ) );
                    }
                };
            }
        };
        decoders[ Type.INT32 ] =
                new Decoder<Integer>( Integer.class, "INT32" ) {
            public Reader<Integer> createReader( ByteBuffer bbuf, long nrow ) {
                final IntBuffer ibuf = bbuf.asIntBuffer();
                return new IntReader() {
                    int get( long ix ) {
                        return ibuf.get( BufUtils.longToInt( ix ) );
                    }
                };
            }
        };
        decoders[ Type.INT64 ] = new Decoder<Long>( Long.class, "INT64" ) {
            public Reader<Long> createReader( ByteBuffer bbuf, long nrow ) {
                final LongBuffer lbuf = bbuf.asLongBuffer();
                return new LongReader() {
                    long get( long ix ) {
                        return lbuf.get( BufUtils.longToInt( ix ) );
                    }
                };
            }
        };
        decoders[ Type.UINT8 ] = new Decoder<Short>( Short.class, "UINT8" ) {
            public Reader<Short> createReader( final ByteBuffer bbuf,
                                               long nrow ) {
                return new ShortReader() {
                    short get( long ix ) {
                        return (short)
                               ( 0xff & bbuf.get( BufUtils.longToInt( ix ) ) );
                    }
                };
            }
        };
        decoders[ Type.UINT16 ] =
                new Decoder<Integer>( Integer.class, "UINT16" ) {
            public Reader<Integer> createReader( ByteBuffer bbuf, long nrow ) {
                final ShortBuffer sbuf = bbuf.asShortBuffer();
                return new IntReader() {
                    int get( long ix ) {
                        return 0xffff & sbuf.get( BufUtils.longToInt( ix ) );
                    }
                };
            }
        };
        decoders[ Type.UINT32 ] = new Decoder<Long>( Long.class, "UINT32" ) {
            public Reader<Long> createReader( ByteBuffer bbuf, long nrow ) {
                final IntBuffer ibuf = bbuf.asIntBuffer();
                return new LongReader() {
                    long get( long ix ) {
                        return 0xffffffffL
                             & ibuf.get( BufUtils.longToInt( ix ) );
                    }
                };
            }
        };
        decoders[ Type.UINT64 ] = new UnsupportedDecoder( "UINT64" );
        decoders[ Type.FLOAT ] = new Decoder<Float>( Float.class, "FLOAT" ) {
            public Reader<Float> createReader( ByteBuffer bbuf, long nrow ) {
                final FloatBuffer fbuf = bbuf.asFloatBuffer();
                return new AbstractReader<Float>( Float.class ) {
                    private float get( long ix ) {
                        return fbuf.get( BufUtils.longToInt( ix ) );
                    }
                    public Float getObject( long ix ) {
                        return Float.valueOf( get( ix ) );
                    }
                    public byte getByte( long ix ) {
                        return (byte) get( ix );
                    }
                    public short getShort( long ix ) {
                        return (short) get( ix );
                    }
                    public int getInt( long ix ) {
                        return (int) get( ix );
                    }
                    public long getLong( long ix ) {
                        return (long) get( ix );
                    }
                    public float getFloat( long ix ) {
                        return (float) get( ix );
                    }
                    public double getDouble( long ix ) {
                        return (double) get( ix );
                    }
                };
            }
        };
        decoders[ Type.DOUBLE ] =
                new Decoder<Double>( Double.class, "DOUBLE" ) {
            public Reader<Double> createReader( ByteBuffer bbuf, long nrow ) {
                final DoubleBuffer dbuf = bbuf.asDoubleBuffer();
                return new AbstractReader<Double>( Double.class ) {
                    private double get( long ix ) {
                        return dbuf.get( BufUtils.longToInt( ix ) );
                    }
                    public Double getObject( long ix ) {
                        return Double.valueOf( get( ix ) );
                    }
                    public byte getByte( long ix ) {
                        return (byte) get( ix );
                    }
                    public short getShort( long ix ) {
                        return (short) get( ix );
                    }
                    public int getInt( long ix ) {
                        return (int) get( ix );
                    }
                    public long getLong( long ix ) {
                        return (long) get( ix );
                    }
                    public float getFloat( long ix ) {
                        return (float) get( ix );
                    }
                    public double getDouble( long ix ) {
                        return (double) get( ix );
                    }
                };
            }
        };
        decoders[ Type.UTF8 ] = new Decoder<String>( String.class, "UTF8" ) {
            public Reader<String> createReader( final ByteBuffer bbuf,
                                                long nrow ) {
                return new VariableLengthReader32<String>( String.class,
                                                           bbuf, nrow ) {
                    public String getObject( long ix ) {
                        return new String( getBytes( ix ), BufUtils.UTF8 );
                    }
                };
            }
        };
        decoders[ Type.BINARY ] =
                new Decoder<byte[]>( byte[].class, "BINARY" ) {
            public Reader<byte[]> createReader( final ByteBuffer bbuf,
                                                long nrow ) {
                return new VariableLengthReader32<byte[]>( byte[].class,
                                                           bbuf, nrow ) {
                    public byte[] getObject( long ix ) {
                        return getBytes( ix );
                    }
                };
            }
        };
        decoders[ Type.LARGE_UTF8 ] =
                new Decoder<String>( String.class, "LARGE_UTF8" ) {
            public Reader<String> createReader( final ByteBuffer bbuf,
                                                long nrow ) {
                return new VariableLengthReader64<String>( String.class,
                                                           bbuf, nrow ) {
                    public String getObject( long ix ) {
                        return new String( getBytes( ix ), BufUtils.UTF8 );
                    }
                };
            }
        };
        decoders[ Type.LARGE_BINARY ] =
                new Decoder<byte[]>( byte[].class, "LARGE_BINARY" ) {
            public Reader<byte[]> createReader( final ByteBuffer bbuf,
                                                long nrow ) {
                return new VariableLengthReader64<byte[]>( byte[].class,
                                                           bbuf, nrow ) {
                    public byte[] getObject( long ix ) {
                        return getBytes( ix );
                    }
                };
            }
        };

        /* I could do this, but (1) I don't know if there are any instances
         * of this data type in feather files out there and (2) it's not
         * clear to me how to interpret the feather format documentation. */
        decoders[ Type.CATEGORY ] = new UnsupportedDecoder( "CATEGORY" );
        decoders[ Type.TIMESTAMP ] =
                new Decoder<Long>( Long.class, "TIMESTAMP" ) {
            public Reader<Long> createReader( ByteBuffer bbuf, long nrow ) {
                final LongBuffer lbuf = bbuf.asLongBuffer();
                return new LongReader() {
                    long get( long ix ) {
                        return lbuf.get( BufUtils.longToInt( ix ) );
                    }
                };
            }
        };
        decoders[ Type.DATE ] = new Decoder<Integer>( Integer.class, "DATE" ) {
            public Reader<Integer> createReader( ByteBuffer bbuf, long nrow ) {
                final IntBuffer ibuf = bbuf.asIntBuffer();
                return new IntReader() {
                    int get( long ix ) {
                        return ibuf.get( BufUtils.longToInt( ix ) );
                    }
                };
            }
        };
        decoders[ Type.TIME ] = new Decoder<Long>( Long.class, "TIME" ) {
            public Reader<Long> createReader( ByteBuffer bbuf, long nrow ) {
                final LongBuffer lbuf = bbuf.asLongBuffer();
                return new LongReader() {
                    long get( long ix ) {
                        return lbuf.get( BufUtils.longToInt( ix ) );
                    }
                };
            }
        };
        assert Arrays.asList( decoders ).indexOf( null ) == -1;
        return decoders;
    }

    private static abstract class AbstractReader<T> implements Reader<T> {
        final Class<T> clazz_;
        AbstractReader( Class<T> clazz ) {
            clazz_ = clazz;
        }
        public Class<T> getValueClass() {
            return clazz_;
        }
    }

    private static abstract class NonNumericReader<T>
            extends AbstractReader<T> {
        public NonNumericReader( Class<T> clazz ) {
            super( clazz );
        }
        public byte getByte( long ix ) {
            return (byte) 0;
        }
        public short getShort( long ix ) {
            return (short) 0;
        }
        public int getInt( long ix ) {
            return 0;
        }
        public long getLong( long ix ) {
            return 0L;
        }
        public float getFloat( long ix ) {
            return Float.NaN;
        }
        public double getDouble( long ix ) {
            return Double.NaN;
        }
    }

    private static abstract class ShortReader extends AbstractReader<Short> {
        ShortReader() {
            super( Short.class );
        }
        abstract short get( long ix );
        public Short getObject( long ix ) {
            return Short.valueOf( get( ix ) );
        }
        public byte getByte( long ix ) {
            return (byte) get( ix );
        }
        public short getShort( long ix ) {
            return (short) get( ix );
        }
        public int getInt( long ix ) {
            return (int) get( ix );
        }
        public long getLong( long ix ) {
            return (long) get( ix );
        }
        public float getFloat( long ix ) {
            return (float) get( ix );
        }
        public double getDouble( long ix ) {
            return (double) get( ix );
        }
    }

    private static abstract class IntReader extends AbstractReader<Integer> {
        IntReader() {
            super( Integer.class );
        }
        abstract int get( long ix );
        public Integer getObject( long ix ) {
            return Integer.valueOf( get( ix ) );
        }
        public byte getByte( long ix ) {
            return (byte) get( ix );
        }
        public short getShort( long ix ) {
            return (short) get( ix );
        }
        public int getInt( long ix ) {
            return (int) get( ix );
        }
        public long getLong( long ix ) {
            return (long) get( ix );
        }
        public float getFloat( long ix ) {
            return (float) get( ix );
        }
        public double getDouble( long ix ) {
            return (double) get( ix );
        }
    }

    private static abstract class LongReader extends AbstractReader<Long> {
        LongReader() {
            super( Long.class );
        }
        abstract long get( long ix );
        public Long getObject( long ix ) {
            return Long.valueOf( get( ix ) );
        }
        public byte getByte( long ix ) {
            return (byte) get( ix );
        }
        public short getShort( long ix ) {
            return (short) get( ix );
        }
        public int getInt( long ix ) {
            return (int) get( ix );
        }
        public long getLong( long ix ) {
            return (long) get( ix );
        }
        public float getFloat( long ix ) {
            return (float) get( ix );
        }
        public double getDouble( long ix ) {
            return (double) get( ix );
        }
    }

    private static abstract class VariableLengthReader32<T>
            extends NonNumericReader<T> {
        private static final int OFFSET_SIZE = 4;
        private final ByteBuffer bbuf_;
        private final long data0_;
        VariableLengthReader32( Class<T> clazz, ByteBuffer bbuf, long nrow ) {
            super( clazz );
            bbuf_ = bbuf;
            data0_ = BufUtils.ceil8( ( nrow + 1 ) * OFFSET_SIZE );
        }
        byte[] getBytes( long ix ) {
            int ioff1 = BufUtils.longToInt( ( ix + 1 ) * OFFSET_SIZE );
            int doff0 = bbuf_.getInt( ioff1 - OFFSET_SIZE );
            int doff1 = bbuf_.getInt( ioff1 );
            int leng = doff1 - doff0;
            byte[] dbuf = new byte[ leng ];
            bbuf_.position( BufUtils.longToInt( data0_ + doff0 ) );
            bbuf_.get( dbuf );
            return dbuf;
        }
    }

    private static abstract class VariableLengthReader64<T>
            extends NonNumericReader<T> {
        private static final int OFFSET_SIZE = 8;
        private final ByteBuffer bbuf_;
        private final long data0_;
        VariableLengthReader64( Class<T> clazz, ByteBuffer bbuf, long nrow ) {
            super( clazz );
            bbuf_ = bbuf;
            data0_ = BufUtils.ceil8( ( nrow + 1 ) * OFFSET_SIZE );
        }
        byte[] getBytes( long ix ) {
            int ioff1 = BufUtils.longToInt( ( ix + 1 ) * OFFSET_SIZE );
            long doff0 = bbuf_.getLong( ioff1 - OFFSET_SIZE );
            long doff1 = bbuf_.getLong( ioff1 );
            int leng = BufUtils.longToInt( doff1 - doff0 );
            byte[] dbuf = new byte[ leng ];
            bbuf_.position( BufUtils.longToInt( data0_ + doff0 ) );
            bbuf_.get( dbuf );
            return dbuf;
        }
    }

    private static class UnsupportedDecoder extends Decoder<Void> {
        private static Reader<Void> dummyReader_;
        UnsupportedDecoder( String name ) {
            super( Void.class, name + "(*unsupported*)" );
            dummyReader_ = new NonNumericReader<Void>( Void.class ) {
                public Void getObject( long ix ) {
                    return null;
                }
            };
        }
        public Reader<Void> createReader( ByteBuffer buf, long nrow ) {
            return dummyReader_;
        }
    }
}
