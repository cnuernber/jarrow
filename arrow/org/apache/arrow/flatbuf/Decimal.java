// automatically generated by the FlatBuffers compiler, do not modify

package org.apache.arrow.flatbuf;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Decimal extends Table {
  public static Decimal getRootAsDecimal(ByteBuffer _bb) { return getRootAsDecimal(_bb, new Decimal()); }
  public static Decimal getRootAsDecimal(ByteBuffer _bb, Decimal obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public Decimal __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  /**
   * Total number of decimal digits
   */
  public int precision() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  /**
   * Number of digits after the decimal point "."
   */
  public int scale() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createDecimal(FlatBufferBuilder builder,
      int precision,
      int scale) {
    builder.startObject(2);
    Decimal.addScale(builder, scale);
    Decimal.addPrecision(builder, precision);
    return Decimal.endDecimal(builder);
  }

  public static void startDecimal(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addPrecision(FlatBufferBuilder builder, int precision) { builder.addInt(0, precision, 0); }
  public static void addScale(FlatBufferBuilder builder, int scale) { builder.addInt(1, scale, 0); }
  public static int endDecimal(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

