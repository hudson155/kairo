@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import kairo.reflect.KairoType
import kairo.reflect.kairoType
import kairo.serialization.typeReference

public inline fun <reified T : Any> ObjectMapper.kairoWriteSpecial(value: T?): String =
  kairoWriteSpecial(value, kairoType<T>())

public fun <T : Any> ObjectMapper.kairoWriteSpecial(value: T?, type: KairoType<T>): String =
  kairoWriteSpecial(value, type.typeReference)

/*
 * A "special" version of [ObjectMapper.writeValueAsString] or [ObjectMapper.kairoWrite]
 * that passes through strings untouched.
 * This is a fairly hacky approach. Improvements are welcomed!
 */
public fun <T : Any> ObjectMapper.kairoWriteSpecial(value: T?, typeReference: TypeReference<T>): String {
  try {
    return convertValue(value)
  } catch (e: Exception) {
    try {
      return kairoWrite(value, typeReference)
    } catch (_: Exception) {
      throw e
    }
  }
}

public fun <T : Any> ObjectMapper.kairoWriteSpecial(value: T?, type: JavaType): String {
  try {
    return convertValue(value)
  } catch (e: Exception) {
    try {
      return kairoWrite(value, type)
    } catch (_: Exception) {
      throw e
    }
  }
}
