package kairo.config

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

// TODO: Should config resolution be parallelized?
// If fetching a lot of GCP secrets, it might have significant impact.

@Suppress("UseDataClass")
public class ConfigResolver(
  internal val prefix: String,
  internal val resolve: suspend (raw: String) -> String?,
)

public suspend inline fun <reified T : Any> resolveConfig(
  config: T,
  resolvers: List<ConfigResolver>,
): T {
  var json = Json.encodeToJsonElement(config)
  json = resolveConfig(json, resolvers)
  return Json.decodeFromJsonElement(json)
}

public suspend fun resolveConfig(
  json: JsonElement,
  resolvers: List<ConfigResolver>,
): JsonElement =
  when (json) {
    is JsonObject -> JsonObject(json.mapValues { (_, value) -> resolveConfig(value, resolvers) })
    is JsonArray -> JsonArray(json.map { value -> resolveConfig(value, resolvers) })
    is JsonPrimitive -> resolveConfig(json, resolvers)
  }

private suspend fun resolveConfig(
  json: JsonPrimitive,
  resolvers: List<ConfigResolver>,
): JsonElement {
  if (!json.isString) return json
  val content = json.content
  val resolver = resolvers.singleNullOrThrow { content.startsWith(it.prefix) } ?: return json
  return Json.encodeToJsonElement(resolver.resolve(content.removePrefix(resolver.prefix)))
}
