// automatically generated by the FlatBuffers compiler, do not modify

package jarrow.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class CategoryMetadata extends Table {
  public static CategoryMetadata getRootAsCategoryMetadata(ByteBuffer _bb) { return getRootAsCategoryMetadata(_bb, new CategoryMetadata()); }
  public static CategoryMetadata getRootAsCategoryMetadata(ByteBuffer _bb, CategoryMetadata obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public CategoryMetadata __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  /**
   * The category codes are presumed to be integers that are valid indexes into
   * the levels array
   */
  public PrimitiveArray levels() { return levels(new PrimitiveArray()); }
  public PrimitiveArray levels(PrimitiveArray obj) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(o + bb_pos), bb) : null; }
  public boolean ordered() { int o = __offset(6); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }

  public static int createCategoryMetadata(FlatBufferBuilder builder,
      int levelsOffset,
      boolean ordered) {
    builder.startObject(2);
    CategoryMetadata.addLevels(builder, levelsOffset);
    CategoryMetadata.addOrdered(builder, ordered);
    return CategoryMetadata.endCategoryMetadata(builder);
  }

  public static void startCategoryMetadata(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addLevels(FlatBufferBuilder builder, int levelsOffset) { builder.addOffset(0, levelsOffset, 0); }
  public static void addOrdered(FlatBufferBuilder builder, boolean ordered) { builder.addBoolean(1, ordered, false); }
  public static int endCategoryMetadata(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

