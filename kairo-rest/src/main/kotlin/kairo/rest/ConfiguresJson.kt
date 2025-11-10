package kairo.rest

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

/**
 * Use this interface to configure the REST [Json] instance.
 */
public interface ConfiguresJson {
  public fun JsonBuilder.configure()
}
