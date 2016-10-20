// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: speech.proto

package is.hello.supichi.api;

public final class Speech {
  private Speech() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  /**
   * Protobuf enum {@code keyword}
   */
  public enum keyword
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>NULL = 0;</code>
     */
    NULL(0, 0),
    /**
     * <code>OK_SENSE = 1;</code>
     */
    OK_SENSE(1, 1),
    /**
     * <code>STOP = 2;</code>
     */
    STOP(2, 2),
    /**
     * <code>SNOOZE = 3;</code>
     */
    SNOOZE(3, 3),
    /**
     * <code>ALEXA = 4;</code>
     */
    ALEXA(4, 4),
    /**
     * <code>OKAY = 5;</code>
     */
    OKAY(5, 5),
    ;

    /**
     * <code>NULL = 0;</code>
     */
    public static final int NULL_VALUE = 0;
    /**
     * <code>OK_SENSE = 1;</code>
     */
    public static final int OK_SENSE_VALUE = 1;
    /**
     * <code>STOP = 2;</code>
     */
    public static final int STOP_VALUE = 2;
    /**
     * <code>SNOOZE = 3;</code>
     */
    public static final int SNOOZE_VALUE = 3;
    /**
     * <code>ALEXA = 4;</code>
     */
    public static final int ALEXA_VALUE = 4;
    /**
     * <code>OKAY = 5;</code>
     */
    public static final int OKAY_VALUE = 5;


    public final int getNumber() { return value; }

    public static keyword valueOf(int value) {
      switch (value) {
        case 0: return NULL;
        case 1: return OK_SENSE;
        case 2: return STOP;
        case 3: return SNOOZE;
        case 4: return ALEXA;
        case 5: return OKAY;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<keyword>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<keyword>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<keyword>() {
            public keyword findValueByNumber(int number) {
              return keyword.valueOf(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return is.hello.supichi.api.Speech.getDescriptor().getEnumTypes().get(0);
    }

    private static final keyword[] VALUES = values();

    public static keyword valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private keyword(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:keyword)
  }

  public interface speech_dataOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // optional .keyword word = 1;
    /**
     * <code>optional .keyword word = 1;</code>
     */
    boolean hasWord();
    /**
     * <code>optional .keyword word = 1;</code>
     */
    is.hello.supichi.api.Speech.keyword getWord();

    // optional int32 confidence = 2;
    /**
     * <code>optional int32 confidence = 2;</code>
     *
     * <pre>
     * 0 - 127 
     * </pre>
     */
    boolean hasConfidence();
    /**
     * <code>optional int32 confidence = 2;</code>
     *
     * <pre>
     * 0 - 127 
     * </pre>
     */
    int getConfidence();

    // optional int32 version = 3;
    /**
     * <code>optional int32 version = 3;</code>
     */
    boolean hasVersion();
    /**
     * <code>optional int32 version = 3;</code>
     */
    int getVersion();
  }
  /**
   * Protobuf type {@code speech_data}
   */
  public static final class speech_data extends
      com.google.protobuf.GeneratedMessage
      implements speech_dataOrBuilder {
    // Use speech_data.newBuilder() to construct.
    private speech_data(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private speech_data(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final speech_data defaultInstance;
    public static speech_data getDefaultInstance() {
      return defaultInstance;
    }

    public speech_data getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private speech_data(
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
            case 8: {
              int rawValue = input.readEnum();
              is.hello.supichi.api.Speech.keyword value = is.hello.supichi.api.Speech.keyword.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                word_ = value;
              }
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              confidence_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              version_ = input.readInt32();
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
      return is.hello.supichi.api.Speech.internal_static_speech_data_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return is.hello.supichi.api.Speech.internal_static_speech_data_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              is.hello.supichi.api.Speech.speech_data.class, is.hello.supichi.api.Speech.speech_data.Builder.class);
    }

    public static com.google.protobuf.Parser<speech_data> PARSER =
        new com.google.protobuf.AbstractParser<speech_data>() {
      public speech_data parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new speech_data(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<speech_data> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // optional .keyword word = 1;
    public static final int WORD_FIELD_NUMBER = 1;
    private is.hello.supichi.api.Speech.keyword word_;
    /**
     * <code>optional .keyword word = 1;</code>
     */
    public boolean hasWord() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .keyword word = 1;</code>
     */
    public is.hello.supichi.api.Speech.keyword getWord() {
      return word_;
    }

    // optional int32 confidence = 2;
    public static final int CONFIDENCE_FIELD_NUMBER = 2;
    private int confidence_;
    /**
     * <code>optional int32 confidence = 2;</code>
     *
     * <pre>
     * 0 - 127 
     * </pre>
     */
    public boolean hasConfidence() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int32 confidence = 2;</code>
     *
     * <pre>
     * 0 - 127 
     * </pre>
     */
    public int getConfidence() {
      return confidence_;
    }

    // optional int32 version = 3;
    public static final int VERSION_FIELD_NUMBER = 3;
    private int version_;
    /**
     * <code>optional int32 version = 3;</code>
     */
    public boolean hasVersion() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional int32 version = 3;</code>
     */
    public int getVersion() {
      return version_;
    }

    private void initFields() {
      word_ = is.hello.supichi.api.Speech.keyword.NULL;
      confidence_ = 0;
      version_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeEnum(1, word_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, confidence_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, version_);
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
          .computeEnumSize(1, word_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, confidence_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, version_);
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

    public static is.hello.supichi.api.Speech.speech_data parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static is.hello.supichi.api.Speech.speech_data parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static is.hello.supichi.api.Speech.speech_data parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static is.hello.supichi.api.Speech.speech_data parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static is.hello.supichi.api.Speech.speech_data parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static is.hello.supichi.api.Speech.speech_data parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static is.hello.supichi.api.Speech.speech_data parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static is.hello.supichi.api.Speech.speech_data parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static is.hello.supichi.api.Speech.speech_data parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static is.hello.supichi.api.Speech.speech_data parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(is.hello.supichi.api.Speech.speech_data prototype) {
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
     * Protobuf type {@code speech_data}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements is.hello.supichi.api.Speech.speech_dataOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return is.hello.supichi.api.Speech.internal_static_speech_data_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return is.hello.supichi.api.Speech.internal_static_speech_data_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                is.hello.supichi.api.Speech.speech_data.class, is.hello.supichi.api.Speech.speech_data.Builder.class);
      }

      // Construct using is.hello.supichi.api.Speech.speech_data.newBuilder()
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
        word_ = is.hello.supichi.api.Speech.keyword.NULL;
        bitField0_ = (bitField0_ & ~0x00000001);
        confidence_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        version_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return is.hello.supichi.api.Speech.internal_static_speech_data_descriptor;
      }

      public is.hello.supichi.api.Speech.speech_data getDefaultInstanceForType() {
        return is.hello.supichi.api.Speech.speech_data.getDefaultInstance();
      }

      public is.hello.supichi.api.Speech.speech_data build() {
        is.hello.supichi.api.Speech.speech_data result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public is.hello.supichi.api.Speech.speech_data buildPartial() {
        is.hello.supichi.api.Speech.speech_data result = new is.hello.supichi.api.Speech.speech_data(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.word_ = word_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.confidence_ = confidence_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.version_ = version_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof is.hello.supichi.api.Speech.speech_data) {
          return mergeFrom((is.hello.supichi.api.Speech.speech_data)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(is.hello.supichi.api.Speech.speech_data other) {
        if (other == is.hello.supichi.api.Speech.speech_data.getDefaultInstance()) return this;
        if (other.hasWord()) {
          setWord(other.getWord());
        }
        if (other.hasConfidence()) {
          setConfidence(other.getConfidence());
        }
        if (other.hasVersion()) {
          setVersion(other.getVersion());
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
        is.hello.supichi.api.Speech.speech_data parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (is.hello.supichi.api.Speech.speech_data) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // optional .keyword word = 1;
      private is.hello.supichi.api.Speech.keyword word_ = is.hello.supichi.api.Speech.keyword.NULL;
      /**
       * <code>optional .keyword word = 1;</code>
       */
      public boolean hasWord() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .keyword word = 1;</code>
       */
      public is.hello.supichi.api.Speech.keyword getWord() {
        return word_;
      }
      /**
       * <code>optional .keyword word = 1;</code>
       */
      public Builder setWord(is.hello.supichi.api.Speech.keyword value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        word_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional .keyword word = 1;</code>
       */
      public Builder clearWord() {
        bitField0_ = (bitField0_ & ~0x00000001);
        word_ = is.hello.supichi.api.Speech.keyword.NULL;
        onChanged();
        return this;
      }

      // optional int32 confidence = 2;
      private int confidence_ ;
      /**
       * <code>optional int32 confidence = 2;</code>
       *
       * <pre>
       * 0 - 127 
       * </pre>
       */
      public boolean hasConfidence() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional int32 confidence = 2;</code>
       *
       * <pre>
       * 0 - 127 
       * </pre>
       */
      public int getConfidence() {
        return confidence_;
      }
      /**
       * <code>optional int32 confidence = 2;</code>
       *
       * <pre>
       * 0 - 127 
       * </pre>
       */
      public Builder setConfidence(int value) {
        bitField0_ |= 0x00000002;
        confidence_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 confidence = 2;</code>
       *
       * <pre>
       * 0 - 127 
       * </pre>
       */
      public Builder clearConfidence() {
        bitField0_ = (bitField0_ & ~0x00000002);
        confidence_ = 0;
        onChanged();
        return this;
      }

      // optional int32 version = 3;
      private int version_ ;
      /**
       * <code>optional int32 version = 3;</code>
       */
      public boolean hasVersion() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional int32 version = 3;</code>
       */
      public int getVersion() {
        return version_;
      }
      /**
       * <code>optional int32 version = 3;</code>
       */
      public Builder setVersion(int value) {
        bitField0_ |= 0x00000004;
        version_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 version = 3;</code>
       */
      public Builder clearVersion() {
        bitField0_ = (bitField0_ & ~0x00000004);
        version_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:speech_data)
    }

    static {
      defaultInstance = new speech_data(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:speech_data)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_speech_data_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_speech_data_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014speech.proto\"J\n\013speech_data\022\026\n\004word\030\001 " +
      "\001(\0162\010.keyword\022\022\n\nconfidence\030\002 \001(\005\022\017\n\007ver" +
      "sion\030\003 \001(\005*L\n\007keyword\022\010\n\004NULL\020\000\022\014\n\010OK_SE" +
      "NSE\020\001\022\010\n\004STOP\020\002\022\n\n\006SNOOZE\020\003\022\t\n\005ALEXA\020\004\022\010" +
      "\n\004OKAY\020\005B\026\n\024is.hello.supichi.api"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_speech_data_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_speech_data_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_speech_data_descriptor,
              new java.lang.String[] { "Word", "Confidence", "Version", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
