// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: Configuration.proto
// Protobuf Java Version: 4.28.3

package com.protos;

/**
 * Protobuf type {@code dist_servers.Configuration}
 */
public final class Configuration extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:dist_servers.Configuration)
    ConfigurationOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 28,
      /* patch= */ 3,
      /* suffix= */ "",
      Configuration.class.getName());
  }
  // Use Configuration.newBuilder() to construct.
  private Configuration(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private Configuration() {
    methodType_ = 0;
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.protos.ConfigurationJava.internal_static_dist_servers_Configuration_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.protos.ConfigurationJava.internal_static_dist_servers_Configuration_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.protos.Configuration.class, com.protos.Configuration.Builder.class);
  }

  public static final int FAULT_TOLERANCE_LEVEL_FIELD_NUMBER = 1;
  private int faultToleranceLevel_ = 0;
  /**
   * <code>int32 fault_tolerance_level = 1;</code>
   * @return The faultToleranceLevel.
   */
  @java.lang.Override
  public int getFaultToleranceLevel() {
    return faultToleranceLevel_;
  }

  public static final int METHOD_TYPE_FIELD_NUMBER = 2;
  private int methodType_ = 0;
  /**
   * <code>.dist_servers.MethodType method_type = 2;</code>
   * @return The enum numeric value on the wire for methodType.
   */
  @java.lang.Override public int getMethodTypeValue() {
    return methodType_;
  }
  /**
   * <code>.dist_servers.MethodType method_type = 2;</code>
   * @return The methodType.
   */
  @java.lang.Override public com.protos.MethodType getMethodType() {
    com.protos.MethodType result = com.protos.MethodType.forNumber(methodType_);
    return result == null ? com.protos.MethodType.UNRECOGNIZED : result;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (faultToleranceLevel_ != 0) {
      output.writeInt32(1, faultToleranceLevel_);
    }
    if (methodType_ != com.protos.MethodType.STRT.getNumber()) {
      output.writeEnum(2, methodType_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (faultToleranceLevel_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, faultToleranceLevel_);
    }
    if (methodType_ != com.protos.MethodType.STRT.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(2, methodType_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.protos.Configuration)) {
      return super.equals(obj);
    }
    com.protos.Configuration other = (com.protos.Configuration) obj;

    if (getFaultToleranceLevel()
        != other.getFaultToleranceLevel()) return false;
    if (methodType_ != other.methodType_) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + FAULT_TOLERANCE_LEVEL_FIELD_NUMBER;
    hash = (53 * hash) + getFaultToleranceLevel();
    hash = (37 * hash) + METHOD_TYPE_FIELD_NUMBER;
    hash = (53 * hash) + methodType_;
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.protos.Configuration parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.protos.Configuration parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.protos.Configuration parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.protos.Configuration parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.protos.Configuration parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.protos.Configuration parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.protos.Configuration parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.protos.Configuration parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.protos.Configuration parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.protos.Configuration parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.protos.Configuration parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.protos.Configuration parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.protos.Configuration prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code dist_servers.Configuration}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:dist_servers.Configuration)
      com.protos.ConfigurationOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.protos.ConfigurationJava.internal_static_dist_servers_Configuration_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.protos.ConfigurationJava.internal_static_dist_servers_Configuration_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.protos.Configuration.class, com.protos.Configuration.Builder.class);
    }

    // Construct using com.protos.Configuration.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      faultToleranceLevel_ = 0;
      methodType_ = 0;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.protos.ConfigurationJava.internal_static_dist_servers_Configuration_descriptor;
    }

    @java.lang.Override
    public com.protos.Configuration getDefaultInstanceForType() {
      return com.protos.Configuration.getDefaultInstance();
    }

    @java.lang.Override
    public com.protos.Configuration build() {
      com.protos.Configuration result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.protos.Configuration buildPartial() {
      com.protos.Configuration result = new com.protos.Configuration(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.protos.Configuration result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.faultToleranceLevel_ = faultToleranceLevel_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.methodType_ = methodType_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.protos.Configuration) {
        return mergeFrom((com.protos.Configuration)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.protos.Configuration other) {
      if (other == com.protos.Configuration.getDefaultInstance()) return this;
      if (other.getFaultToleranceLevel() != 0) {
        setFaultToleranceLevel(other.getFaultToleranceLevel());
      }
      if (other.methodType_ != 0) {
        setMethodTypeValue(other.getMethodTypeValue());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              faultToleranceLevel_ = input.readInt32();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 16: {
              methodType_ = input.readEnum();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private int faultToleranceLevel_ ;
    /**
     * <code>int32 fault_tolerance_level = 1;</code>
     * @return The faultToleranceLevel.
     */
    @java.lang.Override
    public int getFaultToleranceLevel() {
      return faultToleranceLevel_;
    }
    /**
     * <code>int32 fault_tolerance_level = 1;</code>
     * @param value The faultToleranceLevel to set.
     * @return This builder for chaining.
     */
    public Builder setFaultToleranceLevel(int value) {

      faultToleranceLevel_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>int32 fault_tolerance_level = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearFaultToleranceLevel() {
      bitField0_ = (bitField0_ & ~0x00000001);
      faultToleranceLevel_ = 0;
      onChanged();
      return this;
    }

    private int methodType_ = 0;
    /**
     * <code>.dist_servers.MethodType method_type = 2;</code>
     * @return The enum numeric value on the wire for methodType.
     */
    @java.lang.Override public int getMethodTypeValue() {
      return methodType_;
    }
    /**
     * <code>.dist_servers.MethodType method_type = 2;</code>
     * @param value The enum numeric value on the wire for methodType to set.
     * @return This builder for chaining.
     */
    public Builder setMethodTypeValue(int value) {
      methodType_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.dist_servers.MethodType method_type = 2;</code>
     * @return The methodType.
     */
    @java.lang.Override
    public com.protos.MethodType getMethodType() {
      com.protos.MethodType result = com.protos.MethodType.forNumber(methodType_);
      return result == null ? com.protos.MethodType.UNRECOGNIZED : result;
    }
    /**
     * <code>.dist_servers.MethodType method_type = 2;</code>
     * @param value The methodType to set.
     * @return This builder for chaining.
     */
    public Builder setMethodType(com.protos.MethodType value) {
      if (value == null) {
        throw new NullPointerException();
      }
      bitField0_ |= 0x00000002;
      methodType_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.dist_servers.MethodType method_type = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearMethodType() {
      bitField0_ = (bitField0_ & ~0x00000002);
      methodType_ = 0;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:dist_servers.Configuration)
  }

  // @@protoc_insertion_point(class_scope:dist_servers.Configuration)
  private static final com.protos.Configuration DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.protos.Configuration();
  }

  public static com.protos.Configuration getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Configuration>
      PARSER = new com.google.protobuf.AbstractParser<Configuration>() {
    @java.lang.Override
    public Configuration parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<Configuration> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Configuration> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.protos.Configuration getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

