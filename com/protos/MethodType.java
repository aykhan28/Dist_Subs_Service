// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: Configuration.proto
// Protobuf Java Version: 4.28.3

package com.protos;

/**
 * Protobuf enum {@code dist_servers.MethodType}
 */
public enum MethodType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>STRT = 0;</code>
   */
  STRT(0),
  /**
   * <code>STOP = 1;</code>
   */
  STOP(1),
  UNRECOGNIZED(-1),
  ;

  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 28,
      /* patch= */ 3,
      /* suffix= */ "",
      MethodType.class.getName());
  }
  /**
   * <code>STRT = 0;</code>
   */
  public static final int STRT_VALUE = 0;
  /**
   * <code>STOP = 1;</code>
   */
  public static final int STOP_VALUE = 1;


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
  public static MethodType valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static MethodType forNumber(int value) {
    switch (value) {
      case 0: return STRT;
      case 1: return STOP;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<MethodType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      MethodType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<MethodType>() {
          public MethodType findValueByNumber(int number) {
            return MethodType.forNumber(number);
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
    return com.protos.ConfigurationJava.getDescriptor().getEnumTypes().get(0);
  }

  private static final MethodType[] VALUES = values();

  public static MethodType valueOf(
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

  private MethodType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:dist_servers.MethodType)
}

