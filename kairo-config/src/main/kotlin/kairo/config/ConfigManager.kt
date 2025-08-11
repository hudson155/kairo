package kairo.config

import kairo.config.ConfigManager.Companion.defaultSources

/**
 * The config manager allows you to resolve config properties from multiple sources.
 * There are a few built-in sources (see [defaultSources]). Feel free to implement your own too.
 */
public class ConfigManager(sources: List<ConfigPropertySource<*>> = defaultSources) {
  public val deserializer: ConfigDeserializer = ConfigDeserializer(sources)

  public val propertyResolver: ConfigPropertyResolver = ConfigPropertyResolver(sources)

  public companion object {
    private val defaultSources: List<ConfigPropertySource<*>> =
      listOf(
        EnvironmentVariableConfigPropertySource(),
        PlaintextConfigPropertySource(),
      )
  }
}
