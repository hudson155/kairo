package io.limberapp.common.typeConversion

import kotlin.reflect.KClass

interface TypeConverter<T : Any> {
  val kClass: KClass<T>

  /**
   * This method can be used for short-circuiting the type conversion. It should not be used for
   * validation, since some types are impossible to statically validate so they will always return
   * true, even when invalid.
   *
   * Returns true if valid, false if invalid, and null if it's not possible to statically validate.
   */
  fun isValid(value: String): Boolean?

  fun parseString(value: String): T

  fun writeString(value: T): String
}
