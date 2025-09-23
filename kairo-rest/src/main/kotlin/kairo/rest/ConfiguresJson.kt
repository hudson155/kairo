package kairo.rest

import kotlinx.serialization.json.JsonBuilder

/**
 * Use this interface to configure the REST JSON instance.
 */
public interface ConfiguresJson {
  public fun JsonBuilder.configure()
}
