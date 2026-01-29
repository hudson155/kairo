package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.StreamReadFeature
import com.fasterxml.jackson.core.StreamWriteFeature
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.core.json.JsonWriteFeature
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.Separators
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.cfg.EnumFeature
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.ktor.util.Attributes
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import kairo.reflect.KairoType
import kairo.reflect.kairoType

/**
 * A wrapper around Jackson's [JsonMapper].
 *
 * Has stricter and less surprising defaults than native Jackson,
 * but you can still override them the same way you would for Jackson.
 *
 * Serialization and deserialization retain type information,
 * reducing surprises like polymorphic type information not being included when serializing the type directly.
 *
 * Supports Kotlin's nullability guarantees.
 */
@OptIn(ExperimentalStdlibApi::class, KairoJson.RawJsonMapper::class)
public class KairoJson @RawJsonMapper internal constructor(
  @RawJsonMapper public val delegate: JsonMapper,
) {
  public class Builder internal constructor() {
    internal val configures: MutableList<JsonMapper.Builder.() -> Unit> = mutableListOf()

    public val attributes: Attributes = Attributes(concurrent = false)

    public var allowUnknown: Boolean = false

    public var pretty: Boolean = false

    public fun addModule(module: Module) {
      configures += { addModule(module) }
    }

    public fun configure(configure: JsonMapper.Builder.() -> Unit) {
      configures += configure
    }
  }

  /**
   * You must opt in to access the raw [JsonMapper].
   */
  @RequiresOptIn
  @Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY)
  public annotation class RawJsonMapper

  public inline fun <reified T> outputStream(outputStream: OutputStream, value: T?) {
    outputStream(outputStream, value, kairoType())
  }

  public fun <T> outputStream(outputStream: OutputStream, value: T?, type: KairoType<T>) {
    val objectWriter = delegate.writerFor(type.jacksonTypeReference())
    objectWriter.writeValue(outputStream, value)
  }

  public inline fun <reified T> serialize(value: T?): String =
    serialize(value, kairoType())

  public fun <T> serialize(value: T?, type: KairoType<T>): String {
    val objectWriter = delegate.writerFor(type.jacksonTypeReference())
    return objectWriter.writeValueAsString(value)
  }

  public inline fun <reified T> inputStream(inputStream: InputStream): T =
    inputStream(inputStream, kairoType<T>())

  public fun <T> inputStream(inputStream: InputStream, type: KairoType<T>): T {
    val objectReader = delegate.readerFor(type.jacksonTypeReference())
    val result = objectReader.readValue<T>(inputStream)
    checkResult(result, type)
    return result
  }

  public inline fun <reified T> deserialize(string: String): T =
    deserialize(string, kairoType<T>())

  public fun <T> deserialize(string: String, type: KairoType<T>): T {
    val objectReader = delegate.readerFor(type.jacksonTypeReference())
    val result = objectReader.readValue<T>(string)
    checkResult(result, type)
    return result
  }

  /**
   * Ensures that the result matches the expected type.
   * The primary purpose is shim support for Kotlin's nullability guarantees.
   *
   * Adapted from [com.fasterxml.jackson.module.kotlin.checkTypeMismatch].
   */
  public fun <T> checkResult(result: T?, type: KairoType<T>) {
    if (result == null && !type.kotlinType.isMarkedNullable) {
      throw RuntimeJsonMappingException(
        "Deserialized value did not match the specified type; " +
          "specified ${type.kotlinClass.qualifiedName}(non-null) but was null.",
      )
    }
  }

  public fun copy(configure: JsonMapper.Builder.() -> Unit): KairoJson =
    KairoJson(delegate.rebuild().apply(configure).build())
}

@Suppress("LongMethod")
public fun JsonMapper.Builder.kairo(builder: KairoJson.Builder) {
  configure(MapperFeature.USE_ANNOTATIONS, true)
  configure(MapperFeature.USE_GETTERS_AS_SETTERS, false)
  configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
  configure(MapperFeature.AUTO_DETECT_CREATORS, false)
  configure(MapperFeature.AUTO_DETECT_FIELDS, false)
  configure(MapperFeature.AUTO_DETECT_GETTERS, true)
  configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false)
  configure(MapperFeature.AUTO_DETECT_SETTERS, false)
  configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, false)
  configure(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS, false)
  configure(MapperFeature.INFER_PROPERTY_MUTATORS, false)
  configure(MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES, false)
  configure(MapperFeature.ALLOW_IS_GETTERS_FOR_NON_BOOLEAN, true)
  configure(MapperFeature.ALLOW_VOID_VALUED_PROPERTIES, true)
  configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, true)
  configure(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS, true)
  configure(MapperFeature.INVERSE_READ_WRITE_ACCESS, false)
  configure(MapperFeature.USE_STATIC_TYPING, false)
  configure(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL, false)
  configure(MapperFeature.INFER_BUILDER_TYPE_BINDINGS, false)
  configure(MapperFeature.REQUIRE_TYPE_ID_FOR_SUBTYPES, true)
  configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
  configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
  configure(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST, false)
  configure(MapperFeature.SORT_CREATOR_PROPERTIES_BY_DECLARATION_ORDER, false)
  configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false)
  configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, false)
  configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, false)
  configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, false)
  configure(MapperFeature.USE_STD_BEAN_NAMING, false)
  configure(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING, true)
  configure(MapperFeature.FIX_FIELD_NAME_UPPER_CASE_PREFIX, false)
  configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, false)
  configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, false)
  configure(MapperFeature.IGNORE_MERGE_FOR_UNMERGEABLE, false)
  configure(MapperFeature.BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES, true)
  configure(MapperFeature.APPLY_DEFAULT_VALUES, true)
  configure(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_OPTIONALS, true)
  configure(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES, true)

  configure(SerializationFeature.WRAP_ROOT_VALUE, false)
  configure(SerializationFeature.INDENT_OUTPUT, builder.pretty)
  configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true)
  configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true)
  configure(SerializationFeature.WRAP_EXCEPTIONS, true)
  configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, true)
  configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, true)
  configure(SerializationFeature.CLOSE_CLOSEABLE, false)
  configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true)
  configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
  configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
  configure(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE, true)
  configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
  configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false)
  configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false)
  configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false)
  configure(SerializationFeature.WRITE_ENUM_KEYS_USING_INDEX, false)
  configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true)
  configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true)
  configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false)
  configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true)
  configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true)
  configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false)
  configure(SerializationFeature.FAIL_ON_ORDER_MAP_BY_INCOMPARABLE_KEY, true)
  configure(SerializationFeature.EAGER_SERIALIZER_FETCH, true)
  configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, false)

  configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
  configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
  configure(DeserializationFeature.USE_LONG_FOR_INTS, true)
  configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, false)
  configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !builder.allowUnknown)
  configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
  configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
  configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true)
  configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true)
  configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true)
  configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true)
  configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
  configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false)
  configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, true)
  // Jackson bug: https://chatgpt.com/share/6965494f-a5e8-800c-ab1f-260ef33171c3.
  configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false)
  configure(DeserializationFeature.FAIL_ON_SUBTYPE_CLASS_NOT_REGISTERED, true)
  configure(DeserializationFeature.WRAP_EXCEPTIONS, true)
  configure(DeserializationFeature.FAIL_ON_UNEXPECTED_VIEW_PROPERTIES, true)
  configure(DeserializationFeature.FAIL_ON_UNKNOWN_INJECT_VALUE, true)
  configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false)
  configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, false)
  configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
  configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false)
  configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false)
  configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false)
  configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, false)
  configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false)
  configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, false)
  configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true)
  configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
  configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, true)

  configure(EnumFeature.READ_ENUM_KEYS_USING_INDEX, false)
  configure(EnumFeature.WRITE_ENUMS_TO_LOWERCASE, false)

  configure(JsonNodeFeature.READ_NULL_PROPERTIES, true)
  configure(JsonNodeFeature.WRITE_NULL_PROPERTIES, true)
  configure(JsonNodeFeature.WRITE_PROPERTIES_SORTED, false)
  configure(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES, false)
  configure(JsonNodeFeature.FAIL_ON_NAN_TO_BIG_DECIMAL_COERCION, true)
  configure(JsonNodeFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)

  configure(JsonReadFeature.ALLOW_JAVA_COMMENTS, false)
  configure(JsonReadFeature.ALLOW_YAML_COMMENTS, false)
  configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, false)
  configure(JsonReadFeature.ALLOW_SINGLE_QUOTES, false)
  configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, false)
  configure(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES, false)
  configure(JsonReadFeature.ALLOW_RS_CONTROL_CHAR, false)
  configure(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS, false)
  configure(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS, false)
  configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS, false)
  configure(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS, false)
  configure(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS, false)
  configure(JsonReadFeature.ALLOW_MISSING_VALUES, false)
  configure(JsonReadFeature.ALLOW_TRAILING_COMMA, false)

  configure(JsonWriteFeature.ESCAPE_NON_ASCII, false)
  configure(JsonWriteFeature.QUOTE_FIELD_NAMES, true)
  configure(JsonWriteFeature.WRITE_HEX_UPPER_CASE, true)
  configure(JsonWriteFeature.WRITE_NAN_AS_STRINGS, false)
  configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS, false)
  configure(JsonWriteFeature.ESCAPE_FORWARD_SLASHES, false)
  configure(JsonWriteFeature.COMBINE_UNICODE_SURROGATES_IN_UTF8, true)

  configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
  configure(JsonParser.Feature.ALLOW_COMMENTS, false)
  configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, false)
  configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, false)
  configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false)
  configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, false)
  configure(JsonParser.Feature.ALLOW_RS_CONTROL_CHAR, false)
  configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, false)
  configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, false)
  configure(JsonParser.Feature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS, false)
  configure(JsonParser.Feature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS, false)
  configure(JsonParser.Feature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS, false)
  configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, false)
  configure(JsonParser.Feature.ALLOW_MISSING_VALUES, false)
  configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, false)
  configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true)
  configure(JsonParser.Feature.IGNORE_UNDEFINED, false)
  configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true)
  configure(JsonParser.Feature.USE_FAST_DOUBLE_PARSER, true)
  configure(JsonParser.Feature.USE_FAST_BIG_NUMBER_PARSER, true)
  configure(JsonParser.Feature.CLEAR_CURRENT_TOKEN_ON_CLOSE, true)

  configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true)
  configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, true)
  configure(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM, true)
  configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true)
  configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, false)
  configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false)
  configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, false)
  configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
  configure(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION, true)
  configure(JsonGenerator.Feature.IGNORE_UNKNOWN, false)
  configure(JsonGenerator.Feature.USE_FAST_DOUBLE_WRITER, true)
  configure(JsonGenerator.Feature.WRITE_HEX_UPPER_CASE, true)
  configure(JsonGenerator.Feature.ESCAPE_FORWARD_SLASHES, false)
  configure(JsonGenerator.Feature.COMBINE_UNICODE_SURROGATES_IN_UTF8, true)

  configure(StreamReadFeature.AUTO_CLOSE_SOURCE, true)
  configure(StreamReadFeature.IGNORE_UNDEFINED, false)
  configure(StreamReadFeature.STRICT_DUPLICATE_DETECTION, true)
  configure(StreamReadFeature.CLEAR_CURRENT_TOKEN_ON_CLOSE, true)
  configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, true)
  configure(StreamReadFeature.USE_FAST_BIG_NUMBER_PARSER, true)
  configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true)

  configure(StreamWriteFeature.AUTO_CLOSE_CONTENT, true)
  configure(StreamWriteFeature.AUTO_CLOSE_TARGET, true)
  configure(StreamWriteFeature.FLUSH_PASSED_TO_STREAM, true)
  configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true)
  configure(StreamWriteFeature.IGNORE_UNKNOWN, false)
  configure(StreamWriteFeature.STRICT_DUPLICATE_DETECTION, true)
  configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true)

  addModule(
    kotlinModule {
      configure(KotlinFeature.NullToEmptyCollection, false)
      configure(KotlinFeature.NullToEmptyMap, false)
      configure(KotlinFeature.NullIsSameAsDefault, false)
      configure(KotlinFeature.SingletonSupport, true)
      configure(KotlinFeature.StrictNullChecks, false)
      configure(KotlinFeature.KotlinPropertyNameAsImplicitName, true)
      configure(KotlinFeature.UseJavaDurationConversion, true)
      configure(KotlinFeature.NewStrictNullChecks, true)
    },
  )

  addModule(KairoKotlinModule(builder))

  addModule(
    JavaTimeModule().apply {
      disable(JavaTimeFeature.NORMALIZE_DESERIALIZED_ZONE_ID)
      disable(JavaTimeFeature.USE_TIME_ZONE_FOR_LENIENT_DATE_PARSING)
      disable(JavaTimeFeature.ALWAYS_ALLOW_STRINGIFIED_DATE_TIMESTAMPS)
      disable(JavaTimeFeature.ONE_BASED_MONTHS)
    },
  )

  addModule(KotlinDatetimeModule())

  defaultPrettyPrinter(
    DefaultPrettyPrinter()
      .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
      .withSeparators(
        Separators.createDefaultInstance()
          .withObjectFieldValueSpacing(Separators.Spacing.AFTER),
      ),
  )

  defaultLeniency(false)

  defaultDateFormat(StdDateFormat().withLenient(false))

  builder.configures.forEach { it() }
}

@OptIn(KairoJson.RawJsonMapper::class)
public fun KairoJson(
  block: KairoJson.Builder.() -> Unit = {},
): KairoJson {
  val delegate = JsonMapper.builder().apply {
    val builder = KairoJson.Builder().apply(block)
    kairo(builder)
  }.build()
  return KairoJson(delegate)
}

public inline fun <reified T> KairoJson.jsonGenerator(jsonGenerator: JsonGenerator, value: T?) {
  jsonGenerator(jsonGenerator, value, kairoType())
}

@OptIn(KairoJson.RawJsonMapper::class)
public fun <T> KairoJson.jsonGenerator(jsonGenerator: JsonGenerator, value: T?, type: KairoType<T>) {
  val objectWriter = delegate.writerFor(type.jacksonTypeReference())
  objectWriter.writeValue(jsonGenerator, value)
}

public inline fun <reified T> KairoJson.reader(reader: Reader): T =
  reader(reader, kairoType<T>())

@OptIn(KairoJson.RawJsonMapper::class)
public fun <T> KairoJson.reader(reader: Reader, type: KairoType<T>): T {
  val objectReader = delegate.readerFor(type.jacksonTypeReference())
  val result = objectReader.readValue<T>(reader)
  checkResult(result, type)
  return result
}
