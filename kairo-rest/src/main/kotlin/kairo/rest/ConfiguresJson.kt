package kairo.rest

import kotlinx.serialization.json.JsonBuilder

public interface ConfiguresJson {
  public fun JsonBuilder.configure()
}
