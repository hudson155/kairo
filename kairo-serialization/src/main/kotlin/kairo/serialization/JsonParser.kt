package kairo.serialization

import com.fasterxml.jackson.core.JsonParser

@Suppress("ForbiddenMethodCall")
public inline fun <reified T : Any> JsonParser.readValue(): T =
  readValueAs(T::class.java)
