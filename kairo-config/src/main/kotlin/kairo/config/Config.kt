package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.StreamReadFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigList
import com.typesafe.config.ConfigObject
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigValueFactory
import com.typesafe.config.ConfigValueType
import kairo.hocon.deserialize
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.KairoJson

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
  val hocon = ConfigFactory.parseResources(configName).resolve().resolve(resolvers)
  val configJson = json.copy {
    // Environment variables always come in as strings.
    configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, true)
    // Don't include source in location, since configs can contain sensitive values.
    configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, false)
    configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, false)
  }
  return configJson.deserialize(hocon, type)
}

private suspend fun Config.resolve(resolvers: List<ConfigResolver>): Config =
  (resolve(root(), resolvers) as ConfigObject).toConfig()

private suspend fun resolve(
  hocon: ConfigValue,
  resolvers: List<ConfigResolver>,
): ConfigValue {
  when (hocon.valueType()) {
    ConfigValueType.OBJECT -> {
      val objectValue = hocon as ConfigObject
      return ConfigValueFactory.fromMap(
        objectValue.mapValues { resolve(it.value, resolvers) },
      )
    }
    ConfigValueType.LIST -> {
      val listValue = hocon as ConfigList
      return ConfigValueFactory.fromIterable(
        listValue.map { resolve(it, resolvers) },
      )
    }
    ConfigValueType.NUMBER,
    ConfigValueType.BOOLEAN,
    ConfigValueType.NULL -> {
      // Only strings can be resolved using config resolvers. Other primitives are left alone.
      return hocon
    }
    ConfigValueType.STRING -> {
      val string = hocon.unwrapped() as String
      val resolver = resolvers.singleNullOrThrow { string.startsWith(it.prefix) }
      if (resolver == null) {
        // No config resolver matched the prefix; leave the string alone.
        return hocon
      }
      val resolved = resolver.resolve(string.removePrefix(resolver.prefix))
      return ConfigValueFactory.fromAnyRef(resolved)
    }
  }
}
