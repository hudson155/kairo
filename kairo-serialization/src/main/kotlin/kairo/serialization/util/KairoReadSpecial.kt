@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.typeReference

public inline fun <reified T : Any> ObjectMapper.kairoReadSpecial(string: String): T? =
  kairoReadSpecial(string, kairoType<T>())

/*
 * A "special" version of [ObjectMapper.readValue]
 * that passes through strings untouched.
 * This is a fairly hacky approach. Improvements are welcomed!
 */
public fun <T : Any> ObjectMapper.kairoReadSpecial(value: String, type: KairoType<T>): T? =
  kairoReadSpecial(value, type.typeReference)

public fun <T : Any> ObjectMapper.kairoReadSpecial(value: String, typeReference: TypeReference<T>): T? {
  try {
    return readValue(value, typeReference)
  } catch (outerException: Exception) {
    try {
      return convertValue(value, typeReference)
    } catch (innerException: Exception) {
      innerException.addSuppressed(outerException)
      throw innerException
    }
  }
}

public fun ObjectMapper.kairoReadSpecial(value: String, type: JavaType): Any? {
  try {
    return kairoRead(value, type)
  } catch (outerException: Exception) {
    try {
      return convertValue(value, type)
    } catch (innerException: Exception) {
      innerException.addSuppressed(outerException)
      throw innerException
    }
  }
}
