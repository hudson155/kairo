package io.limberapp.common.serialization

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.reflect.KClass

/**
 * Class to handle serialization & deserialization.
 */
@Suppress("UNCHECKED_CAST")
class Json(prettyPrint: Boolean = false) {
  /**
   * Most functionality is delegated to this class (which has the same name). It must be public due to the inline
   * functions.
   */
  val objectMapper: ObjectMapper = jacksonObjectMapper()
      .registerModule(JavaTimeModule())
      .setDefaultPrettyPrinter(LimberJsonPrettyPrinter())
      .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
      .configure(SerializationFeature.INDENT_OUTPUT, prettyPrint)
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  /**
   * Parse string to arbitrary class.
   * Cleaner interface used when the class is available at compile time.
   */
  inline fun <reified T : Any> parse(string: String): T =
      objectMapper.readValue(string)

  /**
   * Parse string to arbitrary class.
   * More verbose interface used when the class is only available at run time.
   */
  fun <T : Any> parse(string: String, kClass: KClass<T>): T =
      objectMapper.readValue(string, kClass.java)

  /**
   * Parse string to arbitrary set of classes.
   * Cleaner interface used when the class is available at compile time.
   */
  inline fun <reified T : Any> parseSet(string: String): Set<T> =
      objectMapper.readValue(string)

  /**
   * Parse string to arbitrary list of classes.
   * Cleaner interface used when the class is available at compile time.
   */
  inline fun <reified T : Any> parseList(string: String): List<T> =
      objectMapper.readValue(string)

  /**
   * Stringify arbitrary class to string.
   */
  fun stringify(model: Any): String =
      objectMapper.writeValueAsString(model)

  /**
   * Stringify arbitrary set of classes to string.
   * Faster implementation used when the class is available at compile time.
   */
  @JvmName("stringifySetDynamically")
  inline fun <reified T : Any> stringifySet(model: Set<T>): String =
      objectMapper.writeValueAsString(model)

  /**
   * Stringify arbitrary list of classes to string.
   * Faster implementation used when the class is available at compile time.
   */
  @JvmName("stringifyListDynamically")
  inline fun <reified T : Any> stringifyList(model: List<T>): String =
      objectMapper.writeValueAsString(model)

  /**
   * Stringify arbitrary set of classes to string.
   * Slower implementation used when the class is available at compile time.
   */
  @JvmName("stringifySetStatically")
  fun stringifySet(model: Set<*>): String =
      objectMapper.writeValueAsString(model as Set<Any>)

  /**
   * Stringify arbitrary list of classes to string.
   * Slower implementation used when the class is available at compile time.
   */
  @JvmName("stringifyListStatically")
  fun stringifyList(model: List<*>): String =
      objectMapper.writeValueAsString(model as List<Any>)
}
