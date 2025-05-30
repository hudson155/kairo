@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.typeReference

public inline fun <reified T : Any> ObjectMapper.readValueSpecial(value: String): T? =
  readValueSpecial(value, kairoType<T>())

public fun <T : Any> ObjectMapper.readValueSpecial(value: String, type: KairoType<T>): T? =
  readValueSpecial(value, type.typeReference)

/*
 * A "special" version of [ObjectMapper.readValue]
 * that passes through strings untouched.
 * This is a fairly hacky approach. Improvements are welcomed!
 */
public fun <T : Any> ObjectMapper.readValueSpecial(value: String, typeReference: TypeReference<T>): T? {
  try {
    return convertValue(value, typeReference)
  } catch (e: IllegalArgumentException) {
    try {
      return readValue(value, typeReference)
    } catch (_: Exception) {
      throw e
    }
  }
}

public fun ObjectMapper.readValueSpecial(value: String, type: JavaType): Any? {
  try {
    return convertValue(value, type)
  } catch (e: IllegalArgumentException) {
    try {
      return readValue(value, type)
    } catch (_: Exception) {
      throw e
    }
  }
}
