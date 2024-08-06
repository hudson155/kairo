package kairo.serialization

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature

/**
 * Jackson has some fairly lenient defaults for things from property naming to type coercion and others.
 * Some of these are for their own backwards-compatibility, and others are just lenient defaults.
 *
 * Here we try to make Jackson reasonably strict instead.
 */
internal fun ObjectMapperFactoryBuilder.increaseStrictness() {
  configure(MapperFeature.USE_GETTERS_AS_SETTERS, false)
  configure(MapperFeature.AUTO_DETECT_CREATORS, false)
  configure(MapperFeature.AUTO_DETECT_FIELDS, false)
  configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false)
  configure(MapperFeature.AUTO_DETECT_SETTERS, false)
  configure(MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES, false)
  configure(MapperFeature.ALLOW_VOID_VALUED_PROPERTIES, true)
  configure(MapperFeature.USE_STATIC_TYPING, true)
  configure(MapperFeature.INFER_BUILDER_TYPE_BINDINGS, false)
  configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
  configure(MapperFeature.USE_STD_BEAN_NAMING, true)
  configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, false)
  configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, false)
  configure(MapperFeature.IGNORE_MERGE_FOR_UNMERGEABLE, false)
  configure(MapperFeature.BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES, true)

  configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, true)

  configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
  configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
  configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
  configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
  configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true)
  configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
  configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true)
  configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false)

  configure(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES, false)
  configure(JsonNodeFeature.FAIL_ON_NAN_TO_BIG_DECIMAL_COERCION, true)
}
