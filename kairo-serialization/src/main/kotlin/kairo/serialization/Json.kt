package kairo.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

public fun json(prettyPrint: Boolean = false, builder: JsonBuilder.() -> Unit = {}): Json =
  Json {
    kairo()
    if (prettyPrint) kairoPrettyPrint()
    builder()
  }
