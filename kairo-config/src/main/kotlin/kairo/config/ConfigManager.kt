package kairo.config

import com.typesafe.config.ConfigFactory

public class ConfigManager {
  public fun load(name: String): Config {
    val config = ConfigFactory.parseResources(name)
    return Config(config)
  }
}
