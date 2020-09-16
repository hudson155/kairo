package io.limberapp.common.dataConversion

import kotlin.reflect.KClass

interface DataConversionService<T : Any> {
  val kClass: KClass<T>

  fun assertValid(value: String, name: String?) {
    if (!isValid(value)) throw DataConversionException(name, kClass)
  }

  /**
   * Should return true iff the string is valid for conversion to the type.
   */
  fun isValid(value: String): Boolean

  /**
   * Converts from a validated string to an instance of the type. Behaviour is undefined if a non-validated string is
   * passed.
   */
  fun fromString(value: String): T

  /**
   * Converts from an instance of the type to a string.
   */
  fun toString(value: T): String
}
