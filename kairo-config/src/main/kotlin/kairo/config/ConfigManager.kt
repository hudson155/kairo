package kairo.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

public class ConfigManager {
  public fun load(name: String): Config =
    ConfigFactory.parseResources(name)
}
