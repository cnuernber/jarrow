package uk.ac.starlink.feather;

import jarrow.feather.FeatherType;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import uk.ac.starlink.table.ByteStore;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StoragePolicy;

public abstract class NumberStarColumnWriter extends StarColumnWriter {

    private final byte[] blank_;
    private final int itemSize_;

    public NumberStarColumnWriter( StarTable table, int icol,
                                   FeatherType featherType, boolean isNullable,
                                   byte[] blank ) {
        super( table, icol, featherType, isNullable );
        blank_ = blank.clone();
        itemSize_ = blank.length;
    }

    // value not null
    public abstract void writeNumber( OutputStream out, Number value )
            throws IOException;

    public DataStat writeDataBytes( OutputStream out ) throws IOException {
        final int icol = getColumnIndex();
        RowSequence rseq = getTable().getRowSequence();
        long nrow = 0;
        try {
            while ( rseq.next() ) {
                nrow++;
                Object item = rseq.getCell( icol );
                if ( item != null ) {
                    writeNumber( out, (Number) item );
                }
                else {
                    out.write( blank_ );
                }
            }
        }
        finally {
            rseq.close();
        }
        long nbyte = nrow * itemSize_;
        return new DataStat( nbyte, nrow );
    }

    public ItemAccumulator createItemAccumulator( StoragePolicy storage ) {
        final ByteStore dataStore = storage.makeByteStore();
        final OutputStream dataOut =
            new BufferedOutputStream( dataStore.getOutputStream() );
        return new AbstractItemAccumulator( storage, isNullable() ) {
            long nbData;
            public void addDataItem( Object item ) throws IOException {
                if ( item != null ) {
                    writeNumber( dataOut, (Number) item );
                }
                else {
                    dataOut.write( blank_ );
                }
                nbData += itemSize_;
            }
            public long writeDataBytes( OutputStream out ) throws IOException {
                dataOut.close();
                dataStore.copy( out );
                dataStore.close();
                return nbData;
            }
            public void closeData() throws IOException {
                dataOut.close();
                dataStore.close();
            }
        };
    }
}
