@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.typeReference

public inline fun <reified T : Any> ObjectMapper.writeValueAsStringSpecial(value: T?): String =
  writeValueAsStringSpecial(value, kairoType<T>())

public fun <T : Any> ObjectMapper.writeValueAsStringSpecial(value: T?, type: KairoType<T>): String =
  writeValueAsStringSpecial(value, type.typeReference)

/*
 * A "special" version of [ObjectMapper.writeValueAsString] or [ObjectMapper.kairoWrite]
 * that passes through strings untouched.
 * This is a fairly hacky approach. Improvements are welcomed!
 */
public fun <T : Any> ObjectMapper.writeValueAsStringSpecial(value: T?, typeReference: TypeReference<T>): String {
  if (value == null) {
    return kairoWrite(null)
  }
  try {
    return convertValue(value)
  } catch (e: IllegalArgumentException) {
    try {
      return kairoWrite(value, typeReference)
    } catch (_: Exception) {
      throw e
    }
  }
}

public fun <T : Any> ObjectMapper.writeValueAsStringSpecial(value: T?, type: JavaType): String {
  if (value == null) {
    return kairoWrite(null)
  }
  try {
    return convertValue(value)
  } catch (e: IllegalArgumentException) {
    try {
      return kairoWrite(value, type)
    } catch (_: Exception) {
      throw e
    }
  }
}
