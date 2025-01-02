// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: protoBufDTO.proto

package com.hzk.proto;

/**
 * Protobuf type {@code com.hzk.proto.CDubboGooglePBRequestType}
 */
public  final class CDubboGooglePBRequestType extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.hzk.proto.CDubboGooglePBRequestType)
    CDubboGooglePBRequestTypeOrBuilder {
private static final long serialVersionUID = 0L;
  // Use CDubboGooglePBRequestType.newBuilder() to construct.
  private CDubboGooglePBRequestType(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private CDubboGooglePBRequestType() {
    double_ = 0D;
    float_ = 0F;
    int32_ = 0;
    bool_ = false;
    string_ = "";
    bytesType_ = com.google.protobuf.ByteString.EMPTY;
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private CDubboGooglePBRequestType(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 9: {

            double_ = input.readDouble();
            break;
          }
          case 21: {

            float_ = input.readFloat();
            break;
          }
          case 24: {

            int32_ = input.readInt32();
            break;
          }
          case 104: {

            bool_ = input.readBool();
            break;
          }
          case 114: {
            String s = input.readStringRequireUtf8();

            string_ = s;
            break;
          }
          case 122: {

            bytesType_ = input.readBytes();
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return ProtoBufDTO.internal_static_com_hzk_proto_CDubboGooglePBRequestType_descriptor;
  }

  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return ProtoBufDTO.internal_static_com_hzk_proto_CDubboGooglePBRequestType_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            CDubboGooglePBRequestType.class, CDubboGooglePBRequestType.Builder.class);
  }

  public static final int DOUBLE_FIELD_NUMBER = 1;
  private double double_;
  /**
   * <code>double double = 1;</code>
   */
  public double getDouble() {
    return double_;
  }

  public static final int FLOAT_FIELD_NUMBER = 2;
  private float float_;
  /**
   * <code>float float = 2;</code>
   */
  public float getFloat() {
    return float_;
  }

  public static final int INT32_FIELD_NUMBER = 3;
  private int int32_;
  /**
   * <code>int32 int32 = 3;</code>
   */
  public int getInt32() {
    return int32_;
  }

  public static final int BOOL_FIELD_NUMBER = 13;
  private boolean bool_;
  /**
   * <code>bool bool = 13;</code>
   */
  public boolean getBool() {
    return bool_;
  }

  public static final int STRING_FIELD_NUMBER = 14;
  private volatile Object string_;
  /**
   * <code>string string = 14;</code>
   */
  public String getString() {
    Object ref = string_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs =
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      string_ = s;
      return s;
    }
  }
  /**
   * <code>string string = 14;</code>
   */
  public com.google.protobuf.ByteString
      getStringBytes() {
    Object ref = string_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      string_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int BYTESTYPE_FIELD_NUMBER = 15;
  private com.google.protobuf.ByteString bytesType_;
  /**
   * <code>bytes bytesType = 15;</code>
   */
  public com.google.protobuf.ByteString getBytesType() {
    return bytesType_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (double_ != 0D) {
      output.writeDouble(1, double_);
    }
    if (float_ != 0F) {
      output.writeFloat(2, float_);
    }
    if (int32_ != 0) {
      output.writeInt32(3, int32_);
    }
    if (bool_ != false) {
      output.writeBool(13, bool_);
    }
    if (!getStringBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 14, string_);
    }
    if (!bytesType_.isEmpty()) {
      output.writeBytes(15, bytesType_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (double_ != 0D) {
      size += com.google.protobuf.CodedOutputStream
        .computeDoubleSize(1, double_);
    }
    if (float_ != 0F) {
      size += com.google.protobuf.CodedOutputStream
        .computeFloatSize(2, float_);
    }
    if (int32_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, int32_);
    }
    if (bool_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(13, bool_);
    }
    if (!getStringBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(14, string_);
    }
    if (!bytesType_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(15, bytesType_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof CDubboGooglePBRequestType)) {
      return super.equals(obj);
    }
    CDubboGooglePBRequestType other = (CDubboGooglePBRequestType) obj;

    boolean result = true;
    result = result && (
        Double.doubleToLongBits(getDouble())
        == Double.doubleToLongBits(
            other.getDouble()));
    result = result && (
        Float.floatToIntBits(getFloat())
        == Float.floatToIntBits(
            other.getFloat()));
    result = result && (getInt32()
        == other.getInt32());
    result = result && (getBool()
        == other.getBool());
    result = result && getString()
        .equals(other.getString());
    result = result && getBytesType()
        .equals(other.getBytesType());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + DOUBLE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        Double.doubleToLongBits(getDouble()));
    hash = (37 * hash) + FLOAT_FIELD_NUMBER;
    hash = (53 * hash) + Float.floatToIntBits(
        getFloat());
    hash = (37 * hash) + INT32_FIELD_NUMBER;
    hash = (53 * hash) + getInt32();
    hash = (37 * hash) + BOOL_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getBool());
    hash = (37 * hash) + STRING_FIELD_NUMBER;
    hash = (53 * hash) + getString().hashCode();
    hash = (37 * hash) + BYTESTYPE_FIELD_NUMBER;
    hash = (53 * hash) + getBytesType().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static CDubboGooglePBRequestType parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static CDubboGooglePBRequestType parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static CDubboGooglePBRequestType parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static CDubboGooglePBRequestType parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static CDubboGooglePBRequestType parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static CDubboGooglePBRequestType parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static CDubboGooglePBRequestType parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static CDubboGooglePBRequestType parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static CDubboGooglePBRequestType parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static CDubboGooglePBRequestType parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static CDubboGooglePBRequestType parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static CDubboGooglePBRequestType parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(CDubboGooglePBRequestType prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.hzk.proto.CDubboGooglePBRequestType}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.hzk.proto.CDubboGooglePBRequestType)
      CDubboGooglePBRequestTypeOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ProtoBufDTO.internal_static_com_hzk_proto_CDubboGooglePBRequestType_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ProtoBufDTO.internal_static_com_hzk_proto_CDubboGooglePBRequestType_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              CDubboGooglePBRequestType.class, CDubboGooglePBRequestType.Builder.class);
    }

    // Construct using com.hzk.proto.CDubboGooglePBRequestType.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      double_ = 0D;

      float_ = 0F;

      int32_ = 0;

      bool_ = false;

      string_ = "";

      bytesType_ = com.google.protobuf.ByteString.EMPTY;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return ProtoBufDTO.internal_static_com_hzk_proto_CDubboGooglePBRequestType_descriptor;
    }

    public CDubboGooglePBRequestType getDefaultInstanceForType() {
      return CDubboGooglePBRequestType.getDefaultInstance();
    }

    public CDubboGooglePBRequestType build() {
      CDubboGooglePBRequestType result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public CDubboGooglePBRequestType buildPartial() {
      CDubboGooglePBRequestType result = new CDubboGooglePBRequestType(this);
      result.double_ = double_;
      result.float_ = float_;
      result.int32_ = int32_;
      result.bool_ = bool_;
      result.string_ = string_;
      result.bytesType_ = bytesType_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof CDubboGooglePBRequestType) {
        return mergeFrom((CDubboGooglePBRequestType)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(CDubboGooglePBRequestType other) {
      if (other == CDubboGooglePBRequestType.getDefaultInstance()) return this;
      if (other.getDouble() != 0D) {
        setDouble(other.getDouble());
      }
      if (other.getFloat() != 0F) {
        setFloat(other.getFloat());
      }
      if (other.getInt32() != 0) {
        setInt32(other.getInt32());
      }
      if (other.getBool() != false) {
        setBool(other.getBool());
      }
      if (!other.getString().isEmpty()) {
        string_ = other.string_;
        onChanged();
      }
      if (other.getBytesType() != com.google.protobuf.ByteString.EMPTY) {
        setBytesType(other.getBytesType());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      CDubboGooglePBRequestType parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (CDubboGooglePBRequestType) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private double double_ ;
    /**
     * <code>double double = 1;</code>
     */
    public double getDouble() {
      return double_;
    }
    /**
     * <code>double double = 1;</code>
     */
    public Builder setDouble(double value) {

      double_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>double double = 1;</code>
     */
    public Builder clearDouble() {

      double_ = 0D;
      onChanged();
      return this;
    }

    private float float_ ;
    /**
     * <code>float float = 2;</code>
     */
    public float getFloat() {
      return float_;
    }
    /**
     * <code>float float = 2;</code>
     */
    public Builder setFloat(float value) {

      float_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>float float = 2;</code>
     */
    public Builder clearFloat() {

      float_ = 0F;
      onChanged();
      return this;
    }

    private int int32_ ;
    /**
     * <code>int32 int32 = 3;</code>
     */
    public int getInt32() {
      return int32_;
    }
    /**
     * <code>int32 int32 = 3;</code>
     */
    public Builder setInt32(int value) {

      int32_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 int32 = 3;</code>
     */
    public Builder clearInt32() {

      int32_ = 0;
      onChanged();
      return this;
    }

    private boolean bool_ ;
    /**
     * <code>bool bool = 13;</code>
     */
    public boolean getBool() {
      return bool_;
    }
    /**
     * <code>bool bool = 13;</code>
     */
    public Builder setBool(boolean value) {

      bool_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bool bool = 13;</code>
     */
    public Builder clearBool() {

      bool_ = false;
      onChanged();
      return this;
    }

    private Object string_ = "";
    /**
     * <code>string string = 14;</code>
     */
    public String getString() {
      Object ref = string_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        string_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string string = 14;</code>
     */
    public com.google.protobuf.ByteString
        getStringBytes() {
      Object ref = string_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        string_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string string = 14;</code>
     */
    public Builder setString(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }

      string_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string string = 14;</code>
     */
    public Builder clearString() {

      string_ = getDefaultInstance().getString();
      onChanged();
      return this;
    }
    /**
     * <code>string string = 14;</code>
     */
    public Builder setStringBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

      string_ = value;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString bytesType_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes bytesType = 15;</code>
     */
    public com.google.protobuf.ByteString getBytesType() {
      return bytesType_;
    }
    /**
     * <code>bytes bytesType = 15;</code>
     */
    public Builder setBytesType(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }

      bytesType_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes bytesType = 15;</code>
     */
    public Builder clearBytesType() {

      bytesType_ = getDefaultInstance().getBytesType();
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:com.hzk.proto.CDubboGooglePBRequestType)
  }

  // @@protoc_insertion_point(class_scope:com.hzk.proto.CDubboGooglePBRequestType)
  private static final CDubboGooglePBRequestType DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new CDubboGooglePBRequestType();
  }

  public static CDubboGooglePBRequestType getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<CDubboGooglePBRequestType>
      PARSER = new com.google.protobuf.AbstractParser<CDubboGooglePBRequestType>() {
    public CDubboGooglePBRequestType parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new CDubboGooglePBRequestType(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<CDubboGooglePBRequestType> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<CDubboGooglePBRequestType> getParserForType() {
    return PARSER;
  }

  public CDubboGooglePBRequestType getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

