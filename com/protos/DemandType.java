// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: dist_servers/Subscriber.proto
// Protobuf Java Version: 4.28.3

package com.protos;

/**
 * Protobuf enum {@code dist_servers.DemandType}
 */
public enum DemandType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>SUBS = 0;</code>
   */
  SUBS(0),
  /**
   * <code>DEL = 1;</code>
   */
  DEL(1),
  /**
   * <code>UPDT = 2;</code>
   */
  UPDT(2),
  /**
   * <code>ONLN = 3;</code>
   */
  ONLN(3),
  /**
   * <code>OFFL = 4;</code>
   */
  OFFL(4),
  UNRECOGNIZED(-1),
  ;

  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 28,
      /* patch= */ 3,
      /* suffix= */ "",
      DemandType.class.getName());
  }
  /**
   * <code>SUBS = 0;</code>
   */
  public static final int SUBS_VALUE = 0;
  /**
   * <code>DEL = 1;</code>
   */
  public static final int DEL_VALUE = 1;
  /**
   * <code>UPDT = 2;</code>
   */
  public static final int UPDT_VALUE = 2;
  /**
   * <code>ONLN = 3;</code>
   */
  public static final int ONLN_VALUE = 3;
  /**
   * <code>OFFL = 4;</code>
   */
  public static final int OFFL_VALUE = 4;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static DemandType valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static DemandType forNumber(int value) {
    switch (value) {
      case 0: return SUBS;
      case 1: return DEL;
      case 2: return UPDT;
      case 3: return ONLN;
      case 4: return OFFL;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<DemandType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      DemandType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<DemandType>() {
          public DemandType findValueByNumber(int number) {
            return DemandType.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return com.protos.SubscriberJava.getDescriptor().getEnumTypes().get(0);
  }

  private static final DemandType[] VALUES = values();

  public static DemandType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private DemandType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:dist_servers.DemandType)
}

