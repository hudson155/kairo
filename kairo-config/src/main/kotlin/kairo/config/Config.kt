package kairo.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import com.typesafe.config.ConfigValueFactory
import com.typesafe.config.ConfigValueType
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.KairoJson

/**
 * Call this to load your config file.
 */
public suspend inline fun <reified T : Any> loadConfig(
  /**
   * The name of the config file, in your resources' "config" package.
   * If unset, defaults to the "CONFIG" environment variable.
   */
  configName: String = requireNotNull(System.getenv("CONFIG")) { "CONFIG environment variable not set." },
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
  val hocon = ConfigFactory.parseResources("config/$configName.conf")
    .let { it.resolve() }
    .let { applyConfigResolvers(it, resolvers) }
  val string = hocon.root().render(ConfigRenderOptions.concise())
  return json.deserialize(string, type)
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
