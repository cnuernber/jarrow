// automatically generated by the FlatBuffers compiler, do not modify

package org.apache.arrow.flatbuf;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
/**
 * ----------------------------------------------------------------------
 * Arrow File metadata
 *
 */
public final class Footer extends Table {
  public static Footer getRootAsFooter(ByteBuffer _bb) { return getRootAsFooter(_bb, new Footer()); }
  public static Footer getRootAsFooter(ByteBuffer _bb, Footer obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public Footer __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public short version() { int o = __offset(4); return o != 0 ? bb.getShort(o + bb_pos) : 0; }
  public Schema schema() { return schema(new Schema()); }
  public Schema schema(Schema obj) { int o = __offset(6); return o != 0 ? obj.__assign(__indirect(o + bb_pos), bb) : null; }
  public Block dictionaries(int j) { return dictionaries(new Block(), j); }
  public Block dictionaries(Block obj, int j) { int o = __offset(8); return o != 0 ? obj.__assign(__vector(o) + j * 24, bb) : null; }
  public int dictionariesLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }
  public Block recordBatches(int j) { return recordBatches(new Block(), j); }
  public Block recordBatches(Block obj, int j) { int o = __offset(10); return o != 0 ? obj.__assign(__vector(o) + j * 24, bb) : null; }
  public int recordBatchesLength() { int o = __offset(10); return o != 0 ? __vector_len(o) : 0; }
  /**
   * User-defined metadata
   */
  public KeyValue customMetadata(int j) { return customMetadata(new KeyValue(), j); }
  public KeyValue customMetadata(KeyValue obj, int j) { int o = __offset(12); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int customMetadataLength() { int o = __offset(12); return o != 0 ? __vector_len(o) : 0; }

  public static int createFooter(FlatBufferBuilder builder,
      short version,
      int schemaOffset,
      int dictionariesOffset,
      int recordBatchesOffset,
      int custom_metadataOffset) {
    builder.startObject(5);
    Footer.addCustomMetadata(builder, custom_metadataOffset);
    Footer.addRecordBatches(builder, recordBatchesOffset);
    Footer.addDictionaries(builder, dictionariesOffset);
    Footer.addSchema(builder, schemaOffset);
    Footer.addVersion(builder, version);
    return Footer.endFooter(builder);
  }

  public static void startFooter(FlatBufferBuilder builder) { builder.startObject(5); }
  public static void addVersion(FlatBufferBuilder builder, short version) { builder.addShort(0, version, 0); }
  public static void addSchema(FlatBufferBuilder builder, int schemaOffset) { builder.addOffset(1, schemaOffset, 0); }
  public static void addDictionaries(FlatBufferBuilder builder, int dictionariesOffset) { builder.addOffset(2, dictionariesOffset, 0); }
  public static void startDictionariesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(24, numElems, 8); }
  public static void addRecordBatches(FlatBufferBuilder builder, int recordBatchesOffset) { builder.addOffset(3, recordBatchesOffset, 0); }
  public static void startRecordBatchesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(24, numElems, 8); }
  public static void addCustomMetadata(FlatBufferBuilder builder, int customMetadataOffset) { builder.addOffset(4, customMetadataOffset, 0); }
  public static int createCustomMetadataVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startCustomMetadataVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endFooter(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishFooterBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
  public static void finishSizePrefixedFooterBuffer(FlatBufferBuilder builder, int offset) { builder.finishSizePrefixed(offset); }
}

