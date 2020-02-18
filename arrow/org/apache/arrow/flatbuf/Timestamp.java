// automatically generated by the FlatBuffers compiler, do not modify

package org.apache.arrow.flatbuf;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
/**
 * Time elapsed from the Unix epoch, 00:00:00.000 on 1 January 1970, excluding
 * leap seconds, as a 64-bit integer. Note that UNIX time does not include
 * leap seconds.
 *
 * The Timestamp metadata supports both "time zone naive" and "time zone
 * aware" timestamps. Read about the timezone attribute for more detail
 */
public final class Timestamp extends Table {
  public static Timestamp getRootAsTimestamp(ByteBuffer _bb) { return getRootAsTimestamp(_bb, new Timestamp()); }
  public static Timestamp getRootAsTimestamp(ByteBuffer _bb, Timestamp obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public Timestamp __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public short unit() { int o = __offset(4); return o != 0 ? bb.getShort(o + bb_pos) : 0; }
  /**
   * The time zone is a string indicating the name of a time zone, one of:
   *
   * * As used in the Olson time zone database (the "tz database" or
   *   "tzdata"), such as "America/New_York"
   * * An absolute time zone offset of the form +XX:XX or -XX:XX, such as +07:30
   *
   * Whether a timezone string is present indicates different semantics about
   * the data:
   *
   * * If the time zone is null or equal to an empty string, the data is "time
   *   zone naive" and shall be displayed *as is* to the user, not localized
   *   to the locale of the user. This data can be though of as UTC but
   *   without having "UTC" as the time zone, it is not considered to be
   *   localized to any time zone
   *
   * * If the time zone is set to a valid value, values can be displayed as
   *   "localized" to that time zone, even though the underlying 64-bit
   *   integers are identical to the same data stored in UTC. Converting
   *   between time zones is a metadata-only operation and does not change the
   *   underlying values
   */
  public String timezone() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer timezoneAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer timezoneInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }

  public static int createTimestamp(FlatBufferBuilder builder,
      short unit,
      int timezoneOffset) {
    builder.startObject(2);
    Timestamp.addTimezone(builder, timezoneOffset);
    Timestamp.addUnit(builder, unit);
    return Timestamp.endTimestamp(builder);
  }

  public static void startTimestamp(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addUnit(FlatBufferBuilder builder, short unit) { builder.addShort(0, unit, 0); }
  public static void addTimezone(FlatBufferBuilder builder, int timezoneOffset) { builder.addOffset(1, timezoneOffset, 0); }
  public static int endTimestamp(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

