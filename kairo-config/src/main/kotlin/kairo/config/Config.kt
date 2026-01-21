package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.StreamReadFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
import com.typesafe.config.ConfigValueType
import kairo.hocon.deserialize
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.KairoJson
import kairo.util.resource

public fun configName(configName: String, prefix: String = "config"): String =
  "$prefix/$configName.conf"

/**
 * Call this to load your config file.
 */
public suspend inline fun <reified T : Any> loadConfig(
  /**
   * The name of the config file, in your resources' "config" package.
   * If unset, defaults to the "CONFIG" environment variable.
   */
  configName: String = configName(requireNotNull(System.getenv("CONFIG")) { "CONFIG environment variable not set." }),
  resolvers: List<ConfigResolver> = emptyList(),
  json: KairoJson = KairoJson(),
): T =
  loadConfig(configName, resolvers, json, kairoType())

public suspend fun <T : Any> loadConfig(
  configName: String,
  resolvers: List<ConfigResolver> = emptyList(),
  json: KairoJson = KairoJson(),
  type: KairoType<T>,
): T {
  // Parsing URL instead of resource to avoid swallowing not found errors.
  val hocon = ConfigFactory.parseURL(resource(configName))
    .let { it.resolve() }
    .let { applyConfigResolvers(it, resolvers) }
  val configJson = json.copy {
    // Environment variables always come in as strings.
    configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, false)
    // Don't include source in location, since configs can contain sensitive values.
    configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, false)
    configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, false)
  }
  return configJson.deserialize(hocon, type)
}

private suspend fun applyConfigResolvers(
  hocon: Config,
  resolvers: List<ConfigResolver>,
): Config =
  hocon.entrySet().fold(hocon) { config, (path, value) ->
    if (value.valueType() != ConfigValueType.STRING) {
      // Only strings can be resolved using config resolvers. Other primitives are left alone.
      return@fold config
    }
    val string = value.unwrapped() as String
    val resolver = resolvers.singleNullOrThrow { string.startsWith(it.prefix) }
    if (resolver == null) {
      // No config resolver matched the prefix; leave the string alone.
      return@fold config
    }
    val resolved = resolver.resolve(string.removePrefix(resolver.prefix))
    return@fold config.withValue(path, ConfigValueFactory.fromAnyRef(resolved))
  }
