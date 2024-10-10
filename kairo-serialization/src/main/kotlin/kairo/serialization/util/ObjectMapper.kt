@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue

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
  try {
    return convertValue(value)
  } catch (e: IllegalArgumentException) {
    try {
      return writeValueAsString(value)
    } catch (_: Exception) {
      throw e
    }
  }
}
