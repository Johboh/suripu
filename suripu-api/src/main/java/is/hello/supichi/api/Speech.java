// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: speech.proto

package is.hello.supichi.api;

public final class Speech {
  private Speech() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  /**
   * Protobuf enum {@code speech.Equalizer}
   */
  public enum Equalizer
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>NONE = 0;</code>
     */
    NONE(0, 0),
    /**
     * <code>SENSE_ONE = 1;</code>
     */
    SENSE_ONE(1, 1),
    ;

    /**
     * <code>NONE = 0;</code>
     */
    public static final int NONE_VALUE = 0;
    /**
     * <code>SENSE_ONE = 1;</code>
     */
    public static final int SENSE_ONE_VALUE = 1;


    public final int getNumber() { return value; }

    public static Equalizer valueOf(int value) {
      switch (value) {
        case 0: return NONE;
        case 1: return SENSE_ONE;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<Equalizer>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<Equalizer>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Equalizer>() {
            public Equalizer findValueByNumber(int number) {
              return Equalizer.valueOf(number);
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

    private static final Equalizer[] VALUES = values();

    public static Equalizer valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private Equalizer(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:speech.Equalizer)
  }

  /**
   * Protobuf enum {@code speech.AudioFormat}
   */
  public enum AudioFormat
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>MP3 = 0;</code>
     */
    MP3(0, 0),
    /**
     * <code>ADPCM = 1;</code>
     */
    ADPCM(1, 1),
    /**
     * <code>RAW = 2;</code>
     */
    RAW(2, 2),
    ;

    /**
     * <code>MP3 = 0;</code>
     */
    public static final int MP3_VALUE = 0;
    /**
     * <code>ADPCM = 1;</code>
     */
    public static final int ADPCM_VALUE = 1;
    /**
     * <code>RAW = 2;</code>
     */
    public static final int RAW_VALUE = 2;


    public final int getNumber() { return value; }

    public static AudioFormat valueOf(int value) {
      switch (value) {
        case 0: return MP3;
        case 1: return ADPCM;
        case 2: return RAW;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<AudioFormat>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<AudioFormat>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<AudioFormat>() {
            public AudioFormat findValueByNumber(int number) {
              return AudioFormat.valueOf(number);
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
      return is.hello.supichi.api.Speech.getDescriptor().getEnumTypes().get(1);
    }

    private static final AudioFormat[] VALUES = values();

    public static AudioFormat valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private AudioFormat(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:speech.AudioFormat)
  }

  /**
   * Protobuf enum {@code speech.Keyword}
   */
  public enum Keyword
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

    public static Keyword valueOf(int value) {
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

    public static com.google.protobuf.Internal.EnumLiteMap<Keyword>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<Keyword>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Keyword>() {
            public Keyword findValueByNumber(int number) {
              return Keyword.valueOf(number);
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
      return is.hello.supichi.api.Speech.getDescriptor().getEnumTypes().get(2);
    }

    private static final Keyword[] VALUES = values();

    public static Keyword valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private Keyword(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:speech.Keyword)
  }

  public interface SpeechRequestOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // optional .speech.Keyword word = 1;
    /**
     * <code>optional .speech.Keyword word = 1;</code>
     */
    boolean hasWord();
    /**
     * <code>optional .speech.Keyword word = 1;</code>
     */
    is.hello.supichi.api.Speech.Keyword getWord();

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

    // optional .speech.Equalizer eq = 4 [default = SENSE_ONE];
    /**
     * <code>optional .speech.Equalizer eq = 4 [default = SENSE_ONE];</code>
     */
    boolean hasEq();
    /**
     * <code>optional .speech.Equalizer eq = 4 [default = SENSE_ONE];</code>
     */
    is.hello.supichi.api.Speech.Equalizer getEq();

    // optional .speech.AudioFormat response = 5 [default = MP3];
    /**
     * <code>optional .speech.AudioFormat response = 5 [default = MP3];</code>
     */
    boolean hasResponse();
    /**
     * <code>optional .speech.AudioFormat response = 5 [default = MP3];</code>
     */
    is.hello.supichi.api.Speech.AudioFormat getResponse();

    // optional int32 sampling_rate = 6 [default = 16000];
    /**
     * <code>optional int32 sampling_rate = 6 [default = 16000];</code>
     */
    boolean hasSamplingRate();
    /**
     * <code>optional int32 sampling_rate = 6 [default = 16000];</code>
     */
    int getSamplingRate();
  }
  /**
   * Protobuf type {@code speech.SpeechRequest}
   */
  public static final class SpeechRequest extends
      com.google.protobuf.GeneratedMessage
      implements SpeechRequestOrBuilder {
    // Use SpeechRequest.newBuilder() to construct.
    private SpeechRequest(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private SpeechRequest(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final SpeechRequest defaultInstance;
    public static SpeechRequest getDefaultInstance() {
      return defaultInstance;
    }

    public SpeechRequest getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private SpeechRequest(
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
              is.hello.supichi.api.Speech.Keyword value = is.hello.supichi.api.Speech.Keyword.valueOf(rawValue);
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
            case 32: {
              int rawValue = input.readEnum();
              is.hello.supichi.api.Speech.Equalizer value = is.hello.supichi.api.Speech.Equalizer.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(4, rawValue);
              } else {
                bitField0_ |= 0x00000008;
                eq_ = value;
              }
              break;
            }
            case 40: {
              int rawValue = input.readEnum();
              is.hello.supichi.api.Speech.AudioFormat value = is.hello.supichi.api.Speech.AudioFormat.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(5, rawValue);
              } else {
                bitField0_ |= 0x00000010;
                response_ = value;
              }
              break;
            }
            case 48: {
              bitField0_ |= 0x00000020;
              samplingRate_ = input.readInt32();
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
      return is.hello.supichi.api.Speech.internal_static_speech_SpeechRequest_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return is.hello.supichi.api.Speech.internal_static_speech_SpeechRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              is.hello.supichi.api.Speech.SpeechRequest.class, is.hello.supichi.api.Speech.SpeechRequest.Builder.class);
    }

    public static com.google.protobuf.Parser<SpeechRequest> PARSER =
        new com.google.protobuf.AbstractParser<SpeechRequest>() {
      public SpeechRequest parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new SpeechRequest(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<SpeechRequest> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // optional .speech.Keyword word = 1;
    public static final int WORD_FIELD_NUMBER = 1;
    private is.hello.supichi.api.Speech.Keyword word_;
    /**
     * <code>optional .speech.Keyword word = 1;</code>
     */
    public boolean hasWord() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .speech.Keyword word = 1;</code>
     */
    public is.hello.supichi.api.Speech.Keyword getWord() {
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

    // optional .speech.Equalizer eq = 4 [default = SENSE_ONE];
    public static final int EQ_FIELD_NUMBER = 4;
    private is.hello.supichi.api.Speech.Equalizer eq_;
    /**
     * <code>optional .speech.Equalizer eq = 4 [default = SENSE_ONE];</code>
     */
    public boolean hasEq() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional .speech.Equalizer eq = 4 [default = SENSE_ONE];</code>
     */
    public is.hello.supichi.api.Speech.Equalizer getEq() {
      return eq_;
    }

    // optional .speech.AudioFormat response = 5 [default = MP3];
    public static final int RESPONSE_FIELD_NUMBER = 5;
    private is.hello.supichi.api.Speech.AudioFormat response_;
    /**
     * <code>optional .speech.AudioFormat response = 5 [default = MP3];</code>
     */
    public boolean hasResponse() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional .speech.AudioFormat response = 5 [default = MP3];</code>
     */
    public is.hello.supichi.api.Speech.AudioFormat getResponse() {
      return response_;
    }

    // optional int32 sampling_rate = 6 [default = 16000];
    public static final int SAMPLING_RATE_FIELD_NUMBER = 6;
    private int samplingRate_;
    /**
     * <code>optional int32 sampling_rate = 6 [default = 16000];</code>
     */
    public boolean hasSamplingRate() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>optional int32 sampling_rate = 6 [default = 16000];</code>
     */
    public int getSamplingRate() {
      return samplingRate_;
    }

    private void initFields() {
      word_ = is.hello.supichi.api.Speech.Keyword.NULL;
      confidence_ = 0;
      version_ = 0;
      eq_ = is.hello.supichi.api.Speech.Equalizer.SENSE_ONE;
      response_ = is.hello.supichi.api.Speech.AudioFormat.MP3;
      samplingRate_ = 16000;
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
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeEnum(4, eq_.getNumber());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeEnum(5, response_.getNumber());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeInt32(6, samplingRate_);
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
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(4, eq_.getNumber());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(5, response_.getNumber());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(6, samplingRate_);
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

    public static is.hello.supichi.api.Speech.SpeechRequest parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static is.hello.supichi.api.Speech.SpeechRequest parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static is.hello.supichi.api.Speech.SpeechRequest parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static is.hello.supichi.api.Speech.SpeechRequest parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static is.hello.supichi.api.Speech.SpeechRequest parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static is.hello.supichi.api.Speech.SpeechRequest parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static is.hello.supichi.api.Speech.SpeechRequest parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static is.hello.supichi.api.Speech.SpeechRequest parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static is.hello.supichi.api.Speech.SpeechRequest parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static is.hello.supichi.api.Speech.SpeechRequest parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(is.hello.supichi.api.Speech.SpeechRequest prototype) {
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
     * Protobuf type {@code speech.SpeechRequest}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements is.hello.supichi.api.Speech.SpeechRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return is.hello.supichi.api.Speech.internal_static_speech_SpeechRequest_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return is.hello.supichi.api.Speech.internal_static_speech_SpeechRequest_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                is.hello.supichi.api.Speech.SpeechRequest.class, is.hello.supichi.api.Speech.SpeechRequest.Builder.class);
      }

      // Construct using is.hello.supichi.api.Speech.SpeechRequest.newBuilder()
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
        word_ = is.hello.supichi.api.Speech.Keyword.NULL;
        bitField0_ = (bitField0_ & ~0x00000001);
        confidence_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        version_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        eq_ = is.hello.supichi.api.Speech.Equalizer.SENSE_ONE;
        bitField0_ = (bitField0_ & ~0x00000008);
        response_ = is.hello.supichi.api.Speech.AudioFormat.MP3;
        bitField0_ = (bitField0_ & ~0x00000010);
        samplingRate_ = 16000;
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return is.hello.supichi.api.Speech.internal_static_speech_SpeechRequest_descriptor;
      }

      public is.hello.supichi.api.Speech.SpeechRequest getDefaultInstanceForType() {
        return is.hello.supichi.api.Speech.SpeechRequest.getDefaultInstance();
      }

      public is.hello.supichi.api.Speech.SpeechRequest build() {
        is.hello.supichi.api.Speech.SpeechRequest result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public is.hello.supichi.api.Speech.SpeechRequest buildPartial() {
        is.hello.supichi.api.Speech.SpeechRequest result = new is.hello.supichi.api.Speech.SpeechRequest(this);
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
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.eq_ = eq_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.response_ = response_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.samplingRate_ = samplingRate_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof is.hello.supichi.api.Speech.SpeechRequest) {
          return mergeFrom((is.hello.supichi.api.Speech.SpeechRequest)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(is.hello.supichi.api.Speech.SpeechRequest other) {
        if (other == is.hello.supichi.api.Speech.SpeechRequest.getDefaultInstance()) return this;
        if (other.hasWord()) {
          setWord(other.getWord());
        }
        if (other.hasConfidence()) {
          setConfidence(other.getConfidence());
        }
        if (other.hasVersion()) {
          setVersion(other.getVersion());
        }
        if (other.hasEq()) {
          setEq(other.getEq());
        }
        if (other.hasResponse()) {
          setResponse(other.getResponse());
        }
        if (other.hasSamplingRate()) {
          setSamplingRate(other.getSamplingRate());
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
        is.hello.supichi.api.Speech.SpeechRequest parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (is.hello.supichi.api.Speech.SpeechRequest) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // optional .speech.Keyword word = 1;
      private is.hello.supichi.api.Speech.Keyword word_ = is.hello.supichi.api.Speech.Keyword.NULL;
      /**
       * <code>optional .speech.Keyword word = 1;</code>
       */
      public boolean hasWord() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .speech.Keyword word = 1;</code>
       */
      public is.hello.supichi.api.Speech.Keyword getWord() {
        return word_;
      }
      /**
       * <code>optional .speech.Keyword word = 1;</code>
       */
      public Builder setWord(is.hello.supichi.api.Speech.Keyword value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        word_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional .speech.Keyword word = 1;</code>
       */
      public Builder clearWord() {
        bitField0_ = (bitField0_ & ~0x00000001);
        word_ = is.hello.supichi.api.Speech.Keyword.NULL;
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

      // optional .speech.Equalizer eq = 4 [default = SENSE_ONE];
      private is.hello.supichi.api.Speech.Equalizer eq_ = is.hello.supichi.api.Speech.Equalizer.SENSE_ONE;
      /**
       * <code>optional .speech.Equalizer eq = 4 [default = SENSE_ONE];</code>
       */
      public boolean hasEq() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional .speech.Equalizer eq = 4 [default = SENSE_ONE];</code>
       */
      public is.hello.supichi.api.Speech.Equalizer getEq() {
        return eq_;
      }
      /**
       * <code>optional .speech.Equalizer eq = 4 [default = SENSE_ONE];</code>
       */
      public Builder setEq(is.hello.supichi.api.Speech.Equalizer value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000008;
        eq_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional .speech.Equalizer eq = 4 [default = SENSE_ONE];</code>
       */
      public Builder clearEq() {
        bitField0_ = (bitField0_ & ~0x00000008);
        eq_ = is.hello.supichi.api.Speech.Equalizer.SENSE_ONE;
        onChanged();
        return this;
      }

      // optional .speech.AudioFormat response = 5 [default = MP3];
      private is.hello.supichi.api.Speech.AudioFormat response_ = is.hello.supichi.api.Speech.AudioFormat.MP3;
      /**
       * <code>optional .speech.AudioFormat response = 5 [default = MP3];</code>
       */
      public boolean hasResponse() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional .speech.AudioFormat response = 5 [default = MP3];</code>
       */
      public is.hello.supichi.api.Speech.AudioFormat getResponse() {
        return response_;
      }
      /**
       * <code>optional .speech.AudioFormat response = 5 [default = MP3];</code>
       */
      public Builder setResponse(is.hello.supichi.api.Speech.AudioFormat value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000010;
        response_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional .speech.AudioFormat response = 5 [default = MP3];</code>
       */
      public Builder clearResponse() {
        bitField0_ = (bitField0_ & ~0x00000010);
        response_ = is.hello.supichi.api.Speech.AudioFormat.MP3;
        onChanged();
        return this;
      }

      // optional int32 sampling_rate = 6 [default = 16000];
      private int samplingRate_ = 16000;
      /**
       * <code>optional int32 sampling_rate = 6 [default = 16000];</code>
       */
      public boolean hasSamplingRate() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      /**
       * <code>optional int32 sampling_rate = 6 [default = 16000];</code>
       */
      public int getSamplingRate() {
        return samplingRate_;
      }
      /**
       * <code>optional int32 sampling_rate = 6 [default = 16000];</code>
       */
      public Builder setSamplingRate(int value) {
        bitField0_ |= 0x00000020;
        samplingRate_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 sampling_rate = 6 [default = 16000];</code>
       */
      public Builder clearSamplingRate() {
        bitField0_ = (bitField0_ & ~0x00000020);
        samplingRate_ = 16000;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:speech.SpeechRequest)
    }

    static {
      defaultInstance = new SpeechRequest(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:speech.SpeechRequest)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_speech_SpeechRequest_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_speech_SpeechRequest_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014speech.proto\022\006speech\"\307\001\n\rSpeechRequest" +
      "\022\035\n\004word\030\001 \001(\0162\017.speech.Keyword\022\022\n\nconfi" +
      "dence\030\002 \001(\005\022\017\n\007version\030\003 \001(\005\022(\n\002eq\030\004 \001(\016" +
      "2\021.speech.Equalizer:\tSENSE_ONE\022*\n\010respon" +
      "se\030\005 \001(\0162\023.speech.AudioFormat:\003MP3\022\034\n\rsa" +
      "mpling_rate\030\006 \001(\005:\00516000*$\n\tEqualizer\022\010\n" +
      "\004NONE\020\000\022\r\n\tSENSE_ONE\020\001**\n\013AudioFormat\022\007\n" +
      "\003MP3\020\000\022\t\n\005ADPCM\020\001\022\007\n\003RAW\020\002*L\n\007Keyword\022\010\n" +
      "\004NULL\020\000\022\014\n\010OK_SENSE\020\001\022\010\n\004STOP\020\002\022\n\n\006SNOOZ" +
      "E\020\003\022\t\n\005ALEXA\020\004\022\010\n\004OKAY\020\005B\036\n\024is.hello.sup",
      "ichi.apiB\006Speech"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_speech_SpeechRequest_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_speech_SpeechRequest_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_speech_SpeechRequest_descriptor,
              new java.lang.String[] { "Word", "Confidence", "Version", "Eq", "Response", "SamplingRate", });
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
