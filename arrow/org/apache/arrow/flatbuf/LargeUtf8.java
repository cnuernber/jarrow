// automatically generated by the FlatBuffers compiler, do not modify

package org.apache.arrow.flatbuf;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
/**
 * Same as Utf8, but with 64-bit offsets, allowing to represent
 * extremely large data values.
 */
public final class LargeUtf8 extends Table {
  public static LargeUtf8 getRootAsLargeUtf8(ByteBuffer _bb) { return getRootAsLargeUtf8(_bb, new LargeUtf8()); }
  public static LargeUtf8 getRootAsLargeUtf8(ByteBuffer _bb, LargeUtf8 obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public LargeUtf8 __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }


  public static void startLargeUtf8(FlatBufferBuilder builder) { builder.startObject(0); }
  public static int endLargeUtf8(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

