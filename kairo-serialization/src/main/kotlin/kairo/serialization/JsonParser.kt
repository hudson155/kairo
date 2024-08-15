package kairo.serialization

import com.fasterxml.jackson.core.JsonParser

public inline fun <reified T : Any> JsonParser.readValueAs(): T =
  readValueAs(T::class.java)
