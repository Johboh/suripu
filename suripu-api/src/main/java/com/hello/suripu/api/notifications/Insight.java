// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: insight.proto

package com.hello.suripu.api.notifications;

public final class Insight {
  private Insight() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface NewInsightOrBuilder extends
      // @@protoc_insertion_point(interface_extends:NewInsight)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string date_category = 1;</code>
     *
     * <pre>
     * Range key for this insight
     * </pre>
     */
    boolean hasDateCategory();
    /**
     * <code>optional string date_category = 1;</code>
     *
     * <pre>
     * Range key for this insight
     * </pre>
     */
    java.lang.String getDateCategory();
    /**
     * <code>optional string date_category = 1;</code>
     *
     * <pre>
     * Range key for this insight
     * </pre>
     */
    com.google.protobuf.ByteString
        getDateCategoryBytes();
  }
  /**
   * Protobuf type {@code NewInsight}
   */
  public static final class NewInsight extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:NewInsight)
      NewInsightOrBuilder {
    // Use NewInsight.newBuilder() to construct.
    private NewInsight(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private NewInsight(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final NewInsight defaultInstance;
    public static NewInsight getDefaultInstance() {
      return defaultInstance;
    }

    public NewInsight getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private NewInsight(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              dateCategory_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.hello.suripu.api.notifications.Insight.internal_static_NewInsight_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.hello.suripu.api.notifications.Insight.internal_static_NewInsight_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.hello.suripu.api.notifications.Insight.NewInsight.class, com.hello.suripu.api.notifications.Insight.NewInsight.Builder.class);
    }

    public static com.google.protobuf.Parser<NewInsight> PARSER =
        new com.google.protobuf.AbstractParser<NewInsight>() {
      public NewInsight parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new NewInsight(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<NewInsight> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int DATE_CATEGORY_FIELD_NUMBER = 1;
    private java.lang.Object dateCategory_;
    /**
     * <code>optional string date_category = 1;</code>
     *
     * <pre>
     * Range key for this insight
     * </pre>
     */
    public boolean hasDateCategory() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string date_category = 1;</code>
     *
     * <pre>
     * Range key for this insight
     * </pre>
     */
    public java.lang.String getDateCategory() {
      java.lang.Object ref = dateCategory_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          dateCategory_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string date_category = 1;</code>
     *
     * <pre>
     * Range key for this insight
     * </pre>
     */
    public com.google.protobuf.ByteString
        getDateCategoryBytes() {
      java.lang.Object ref = dateCategory_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        dateCategory_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      dateCategory_ = "";
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getDateCategoryBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getDateCategoryBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.hello.suripu.api.notifications.Insight.NewInsight parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.hello.suripu.api.notifications.Insight.NewInsight parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.hello.suripu.api.notifications.Insight.NewInsight parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.hello.suripu.api.notifications.Insight.NewInsight parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.hello.suripu.api.notifications.Insight.NewInsight parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.hello.suripu.api.notifications.Insight.NewInsight parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.hello.suripu.api.notifications.Insight.NewInsight parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.hello.suripu.api.notifications.Insight.NewInsight parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.hello.suripu.api.notifications.Insight.NewInsight parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.hello.suripu.api.notifications.Insight.NewInsight parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.hello.suripu.api.notifications.Insight.NewInsight prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code NewInsight}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:NewInsight)
        com.hello.suripu.api.notifications.Insight.NewInsightOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.hello.suripu.api.notifications.Insight.internal_static_NewInsight_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.hello.suripu.api.notifications.Insight.internal_static_NewInsight_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.hello.suripu.api.notifications.Insight.NewInsight.class, com.hello.suripu.api.notifications.Insight.NewInsight.Builder.class);
      }

      // Construct using com.hello.suripu.api.notifications.Insight.NewInsight.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        dateCategory_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.hello.suripu.api.notifications.Insight.internal_static_NewInsight_descriptor;
      }

      public com.hello.suripu.api.notifications.Insight.NewInsight getDefaultInstanceForType() {
        return com.hello.suripu.api.notifications.Insight.NewInsight.getDefaultInstance();
      }

      public com.hello.suripu.api.notifications.Insight.NewInsight build() {
        com.hello.suripu.api.notifications.Insight.NewInsight result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.hello.suripu.api.notifications.Insight.NewInsight buildPartial() {
        com.hello.suripu.api.notifications.Insight.NewInsight result = new com.hello.suripu.api.notifications.Insight.NewInsight(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.dateCategory_ = dateCategory_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.hello.suripu.api.notifications.Insight.NewInsight) {
          return mergeFrom((com.hello.suripu.api.notifications.Insight.NewInsight)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.hello.suripu.api.notifications.Insight.NewInsight other) {
        if (other == com.hello.suripu.api.notifications.Insight.NewInsight.getDefaultInstance()) return this;
        if (other.hasDateCategory()) {
          bitField0_ |= 0x00000001;
          dateCategory_ = other.dateCategory_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.hello.suripu.api.notifications.Insight.NewInsight parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.hello.suripu.api.notifications.Insight.NewInsight) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object dateCategory_ = "";
      /**
       * <code>optional string date_category = 1;</code>
       *
       * <pre>
       * Range key for this insight
       * </pre>
       */
      public boolean hasDateCategory() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string date_category = 1;</code>
       *
       * <pre>
       * Range key for this insight
       * </pre>
       */
      public java.lang.String getDateCategory() {
        java.lang.Object ref = dateCategory_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            dateCategory_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string date_category = 1;</code>
       *
       * <pre>
       * Range key for this insight
       * </pre>
       */
      public com.google.protobuf.ByteString
          getDateCategoryBytes() {
        java.lang.Object ref = dateCategory_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          dateCategory_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string date_category = 1;</code>
       *
       * <pre>
       * Range key for this insight
       * </pre>
       */
      public Builder setDateCategory(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        dateCategory_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string date_category = 1;</code>
       *
       * <pre>
       * Range key for this insight
       * </pre>
       */
      public Builder clearDateCategory() {
        bitField0_ = (bitField0_ & ~0x00000001);
        dateCategory_ = getDefaultInstance().getDateCategory();
        onChanged();
        return this;
      }
      /**
       * <code>optional string date_category = 1;</code>
       *
       * <pre>
       * Range key for this insight
       * </pre>
       */
      public Builder setDateCategoryBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        dateCategory_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:NewInsight)
    }

    static {
      defaultInstance = new NewInsight(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:NewInsight)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_NewInsight_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_NewInsight_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rinsight.proto\"#\n\nNewInsight\022\025\n\rdate_ca" +
      "tegory\030\001 \001(\tB$\n\"com.hello.suripu.api.not" +
      "ifications"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_NewInsight_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_NewInsight_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_NewInsight_descriptor,
        new java.lang.String[] { "DateCategory", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}