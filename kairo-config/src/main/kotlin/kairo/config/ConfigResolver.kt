package kairo.config

import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement

public open class ConfigResolver(
  private val resolvers: List<Resolver> = emptyList(),
  private val json: Json = Json,
) {
  /**
   * Resolvers let you dynamically resolve config string values.
   * String values that start with [prefix]
   * will be mapped through [resolve].
   */
  @Suppress("UseDataClass")
  public class Resolver(
    public val prefix: String,
    public val resolve: suspend (raw: String) -> String?,
  )

  public suspend inline fun <reified T : Any> resolve(config: T): T =
    resolve(config, kairoType())

  public suspend fun <T : Any> resolve(config: T, type: KairoType<T>): T {
    val serializer = json.serializersModule.serializer(type)
    var element = json.encodeToJsonElement(serializer, config)
    element = resolve(element)
    return json.decodeFromJsonElement(serializer, element)
  }

  private suspend fun resolve(element: JsonElement): JsonElement =
    when (element) {
      is JsonObject -> JsonObject(element.mapValues { (_, value) -> resolve(value) })
      is JsonArray -> JsonArray(element.map { value -> resolve(value) })
      is JsonPrimitive -> resolve(element)
    }

  private suspend fun resolve(element: JsonPrimitive): JsonElement {
    if (!element.isString) {
      // Only strings can be resolved using config resolvers. Other primitives are left alone.
      return element
    }
    val content = element.content
    val resolver = resolvers.singleNullOrThrow { content.startsWith(it.prefix) }
    if (resolver == null) {
      // No config resolver matched the prefix; leave the string alone.
      return element
    }
    val resolved = resolver.resolve(content.removePrefix(resolver.prefix))
    return json.encodeToJsonElement(resolved)
  }

  public companion object Default : ConfigResolver()
}
