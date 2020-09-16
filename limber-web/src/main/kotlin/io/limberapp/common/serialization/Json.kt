package io.limberapp.common.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.UnsafeSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.getContextualOrDefault
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * Class to serialize/deserialize [kotlinx.serialization.Serializable] classes.
 */
@OptIn(UnsafeSerializationApi::class)
@Suppress("UNCHECKED_CAST")
class Json(prettyPrint: Boolean = false, serializersModule: SerializersModule = EmptySerializersModule) {
  /**
   * Most functionality is delegated to this class (which has the same name). It must be public due to the inline
   * functions.
   */
  val json = Json {
    this.prettyPrint = prettyPrint
    this.serializersModule = serializersModule
  }

  /**
   * Parse string to arbitrary class.
   * Cleaner interface used when the class is available at compile time.
   */
  inline fun <reified T : Any> parse(string: String): T =
    json.decodeFromString(getSerializer(), string)

  /**
   * Parse string to arbitrary class.
   * More verbose interface used when the class is only available at run time.
   */
  fun <T : Any> parse(string: String, kClass: KClass<T>): T =
    json.decodeFromString(getSerializer(kClass), string)

  /**
   * Parse string to arbitrary set of classes.
   * Cleaner interface used when the class is available at compile time.
   */
  inline fun <reified T : Any> parseSet(string: String): Set<T> =
    json.decodeFromString(SetSerializer(getSerializer()), string)

  /**
   * Parse string to arbitrary list of classes.
   * Cleaner interface used when the class is available at compile time.
   */
  inline fun <reified T : Any> parseList(string: String): List<T> =
    json.decodeFromString(ListSerializer(getSerializer()), string)

  /**
   * Stringify arbitrary class to string.
   */
  fun stringify(model: Any): String =
    json.encodeToString(getSerializer(model::class) as SerializationStrategy<Any>, model)

  /**
   * Stringify arbitrary set of classes to string.
   * Faster implementation used when the class is available at compile time.
   */
  inline fun <reified T : Any> stringifySet(model: Set<T>): String =
    json.encodeToString(SetSerializer(getSerializer()), model)

  /**
   * Stringify arbitrary list of classes to string.
   * Faster implementation used when the class is available at compile time.
   */
  inline fun <reified T : Any> stringifyList(model: List<T>): String =
    json.encodeToString(ListSerializer(getSerializer()), model)

  /**
   * Stringify arbitrary set of classes to string.
   * Slower implementation used when the class is available at compile time.
   */
  fun stringifySet(model: Set<*>): String {
    val serializer = elementSerializer(model)
    return json.encodeToString(SetSerializer(serializer), model as Set<Any>)
  }

  /**
   * Stringify arbitrary list of classes to string.
   * Slower implementation used when the class is available at compile time.
   */
  fun stringifyList(model: List<*>): String {
    val serializer = elementSerializer(model)
    return json.encodeToString(ListSerializer(serializer), model as List<Any>)
  }

  private fun elementSerializer(model: Collection<*>): KSerializer<Any> {
    val serializers = model.mapNotNull { it?.let { getSerializer(it::class) } }
    return if (serializers.isEmpty()) {
      String.serializer()
    } else {
      if (serializers.distinct().size > 1) throw UnsupportedOperationException()
      serializers.first()
    } as KSerializer<Any>
  }

  inline fun <reified T : Any> getSerializer(): KSerializer<T> =
    json.serializersModule.getContextualOrDefault()

  private fun <T : Any> getSerializer(kClass: KClass<T>): KSerializer<T> =
    json.serializersModule.getContextual(kClass) ?: kClass.serializer()
}
