package uk.ac.starlink.feather;

import jarrow.fbs.Type;
import jarrow.feather.FeatherColumn;
import jarrow.feather.FeatherTable;
import jarrow.feather.Reader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import uk.ac.starlink.table.AbstractStarTable;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DefaultValueInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.Tables;

public class FeatherStarTable extends AbstractStarTable {

    private final FeatherTable ftable_;
    private final int ncol_;
    private final long nrow_;
    private final String name_;
    private final FeatherColumn[] fcols_;
    private final ColumnInfo[] colInfos_;
    private final RowReader randomReader_;

    public static final String UCD_KEY = "ucd";
    public static final String UTYPE_KEY = "utype";
    public static final String UNIT_KEY = "unit";
    public static final String DESCRIPTION_KEY = "description";
    public static final String SHAPE_KEY = "shape";
    public static final String META_KEY = "meta";

    public FeatherStarTable( FeatherTable ftable ) {
        ftable_ = ftable;
        ncol_ = ftable.getColumnCount();
        nrow_ = ftable.getRowCount();
        name_ = ftable.getDescription();
        fcols_ = new FeatherColumn[ ncol_ ];
        colInfos_ = new ColumnInfo[ ncol_ ];
        for ( int icol = 0; icol < ncol_; icol++ ) {
            fcols_[ icol ] = ftable.getColumn( icol );
            colInfos_[ icol ] = getColumnInfo( fcols_[ icol ] );
        }
        randomReader_ = new RowReader();
    }

    public int getColumnCount() {
        return ncol_;
    }

    public long getRowCount() {
        return nrow_;
    }

    public boolean isRandom() {
        return true;
    }

    public String getName() {
        return name_;
    }

    public ColumnInfo getColumnInfo( int icol ) {
        return colInfos_[ icol ];
    }

    public synchronized Object getCell( long irow, int icol )
            throws IOException {
        return randomReader_.getCell( irow, icol );
    }

    public synchronized Object getRow( long irow, int icol )
            throws IOException {
        return randomReader_.getRow( irow );
    }

    public RowSequence getRowSequence() {
        final RowReader rowReader = new RowReader();
        return new RowSequence() {
            long irow_ = -1;
            boolean hasData_ = false;
            public boolean next() {
                if ( irow_ < nrow_ - 1 ) {
                    irow_++;
                    hasData_ = true;
                }
                else {
                    hasData_ = false;
                }
                return hasData_;
            }
            public Object getCell( int icol ) throws IOException {
                if ( hasData_ ) {
                    return rowReader.getCell( irow_, icol );
                }
                else {
                    throw new IllegalStateException();
                }
            }
            public Object[] getRow() throws IOException {
                if ( hasData_ ) {
                    return rowReader.getRow( irow_ );
                }
                else {
                    throw new IllegalStateException();
                }
            }
            public void close() {
            }
        };
    }

    private static ColumnInfo getColumnInfo( FeatherColumn fcol ) {
        Class<?> clazz = fcol.getValueClass();
        ColumnInfo info = new ColumnInfo( fcol.getName(), clazz, null );
        info.setNullable( fcol.getNullCount() > 0 );
        Map<String,String> metaMap = getMetaMap( fcol.getUserMeta() );
        for ( Map.Entry<String,String> entry : metaMap.entrySet() ) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ( key.equals( UCD_KEY ) ) {
                info.setUCD( value );
            }
            if ( key.equals( UTYPE_KEY ) ) {
                info.setUtype( value );
            }
            if ( key.equals( UNIT_KEY ) ) {
                info.setUnitString( value );
            }
            if ( key.equals( DESCRIPTION_KEY ) ) {
                info.setDescription( value );
            }
            if ( key.equals( SHAPE_KEY ) ) {
                info.setShape( DefaultValueInfo.unformatShape( value ) );
            }
        }
        if ( fcol.getFeatherType() == Type.UINT8 &&
             clazz.equals( Short.class ) ) {
            info.setAuxDatum( new DescribedValue( Tables.UBYTE_FLAG_INFO,
                                                  Boolean.TRUE ) );
        }
        return info;
    }

    private static Map<String,String> getMetaMap( String userMeta ) {
        Map<String,String> map = new LinkedHashMap<>();
        if ( userMeta != null && userMeta.trim().length() > 0 ) {
            try {
                JSONObject json = new JSONObject( userMeta );
                for ( String key : json.keySet() ) {
                    if ( key.equals( UCD_KEY ) ||
                         key.equals( UTYPE_KEY ) ||
                         key.equals( UNIT_KEY ) ||
                         key.equals( DESCRIPTION_KEY ) ||
                         key.equals( SHAPE_KEY ) ) {
                        map.put( key, json.get( key ).toString() );
                    }
                }
            }
            catch ( JSONException e ) {
                map.put( META_KEY, userMeta ); 
            }
        }
        return map;
    }

    private class RowReader {
        final Reader<?>[] rdrs_ = new Reader<?>[ ncol_ ];
        Reader<?> getReader( int icol ) throws IOException {
            Reader<?> rdr = rdrs_[ icol ];
            if ( rdr != null ) {
                return rdr;
            }
            else {
                rdrs_[ icol ] = fcols_[ icol ].createReader();
                return rdrs_[ icol ];
            }
        }
        Object getCell( long irow, int icol ) throws IOException {
            return getReader( icol ).getObject( irow );
        }
        Object[] getRow( long irow ) throws IOException {
            Object[] row = new Object[ ncol_ ];
            for ( int ic = 0; ic < ncol_; ic++ ) {
                row[ ic ] = getReader( ic ).getObject( irow );
            }
            return row;
        }
    }
}
