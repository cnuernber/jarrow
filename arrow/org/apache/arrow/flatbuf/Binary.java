// automatically generated by the FlatBuffers compiler, do not modify

package org.apache.arrow.flatbuf;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
/**
 * Opaque binary data
 */
public final class Binary extends Table {
  public static Binary getRootAsBinary(ByteBuffer _bb) { return getRootAsBinary(_bb, new Binary()); }
  public static Binary getRootAsBinary(ByteBuffer _bb, Binary obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public Binary __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }


  public static void startBinary(FlatBufferBuilder builder) { builder.startObject(0); }
  public static int endBinary(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

