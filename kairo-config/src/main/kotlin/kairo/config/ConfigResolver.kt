package kairo.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.serializer

// TODO: Should config resolution be parallelized?
//  If fetching a lot of GCP secrets, it might have significant impact.

/**
 * Config resolvers let you dynamically resolve config string values.
 * String values that start with [prefix] will be mapped through [resolve].
 */
@Suppress("UseDataClass")
public class ConfigResolver(
  internal val prefix: String,
  internal val resolve: suspend (raw: String) -> String?,
)

public suspend inline fun <reified T : Any> resolveConfig(
  config: T,
  resolvers: List<ConfigResolver>,
): T =
  resolveConfig(config, serializer(), resolvers)

public suspend fun <T : Any> resolveConfig(
  config: T,
  serializer: KSerializer<T>,
  resolvers: List<ConfigResolver>,
): T {
  var element = Json.encodeToJsonElement(serializer, config)
  element = resolveConfig(element, resolvers)
  return Json.decodeFromJsonElement(serializer, element)
}

public suspend fun resolveConfig(
  element: JsonElement,
  resolvers: List<ConfigResolver>,
): JsonElement =
  when (element) {
    is JsonObject -> JsonObject(element.mapValues { (_, value) -> resolveConfig(value, resolvers) })
    is JsonArray -> JsonArray(element.map { value -> resolveConfig(value, resolvers) })
    is JsonPrimitive -> resolveConfig(element, resolvers)
  }

private suspend fun resolveConfig(
  element: JsonPrimitive,
  resolvers: List<ConfigResolver>,
): JsonElement {
  if (!element.isString) return element
  val content = element.content
  val resolver = resolvers.singleNullOrThrow { content.startsWith(it.prefix) } ?: return element
  return Json.encodeToJsonElement(resolver.resolve(content.removePrefix(resolver.prefix)))
}
