package kairo.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import com.typesafe.config.ConfigValueFactory
import com.typesafe.config.ConfigValueType
import kairo.serialization.KairoJson

public suspend inline fun <reified T : Any> loadConfig(
  configName: String = requireNotNull(System.getenv("CONFIG")) { "CONFIG environment variable not set." },
  json: KairoJson = KairoJson(),
  resolvers: List<ConfigResolver> = emptyList(),
): T {
  val hocon = ConfigFactory.parseResources("config/$configName.conf")
    .let { it.resolve() }
    .let { applyConfigResolvers(it, resolvers) }
  val string = hocon.root().render(ConfigRenderOptions.concise())
  return json.deserialize<T>(string)
}

public suspend fun applyConfigResolvers(
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
