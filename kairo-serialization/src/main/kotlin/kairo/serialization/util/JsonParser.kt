@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.core.JsonParser

public inline fun <reified T> JsonParser.readValue(): T =
  readValueAs(T::class.java)
