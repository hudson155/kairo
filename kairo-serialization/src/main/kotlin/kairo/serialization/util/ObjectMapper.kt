@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.typeReference

public inline fun <reified T : Any> ObjectMapper.kairoWrite(value: T?): String =
  kairoWrite(value, kairoType<T>())

/**
 * Equivalent of [ObjectMapper.writeValueAsString], but doesn't suffer from type erasure.
 * https://stackoverflow.com/questions/34193177/why-does-jackson-polymorphic-serialization-not-work-in-lists
 */
public fun <T : Any> ObjectMapper.kairoWrite(value: T?, type: KairoType<T>): String =
  writerFor(type.typeReference).writeValueAsString(value)

/*
 * These methods are "special" versions of existing Jackson methods.
 * They first try using value conversion.
 * Upon failure, they then try the standard read/write methods.
 * This means that string-like types won't have quote added or expected.
 *
 * This is a fairly hacky approach. Improvements are welcomed!
 */
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

public fun ObjectMapper.writeValueAsStringSpecial(value: Any?): String {
  if (value == null) {
    return kairoWrite(null)
  }
  try {
    return convertValue(value)
  } catch (e: IllegalArgumentException) {
    try {
      return kairoWrite(value)
    } catch (_: Exception) {
      throw e
    }
  }
}
