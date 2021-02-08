package io.limberapp.common.typeConversion

import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * Converts between [T] and its string form.
 *
 * "Normal" type conversion can happen simply by using [isValid], [parseString], and [writeString].
 *
 * Type-unsafe type conversion can happen by opting in to [TypeConverter.Unsafe] and using
 * [canConvert] and [writeStringUnsafe].
 */
interface TypeConverter<T : Any> {
  @RequiresOptIn
  annotation class Unsafe

  /**
   * Instances of this class and its subclasses can be converted by this type converter.
   */
  val kClass: KClass<T>

  @Unsafe
  fun canConvert(type: Type): Boolean {
    if (type !is Class<*>) return false
    return kClass.java.isAssignableFrom(type)
  }

  /**
   * This method can be used for short-circuiting the type conversion. It should not be used for
   * validation, since some types are impossible to statically validate so they will always return
   * true, even when invalid.
   *
   * Returns true if valid, false if invalid, and null if it's not possible to statically validate.
   */
  fun isValid(value: String): Boolean?

  fun parseString(value: String): T

  /**
   * Call [canConvert] before calling this.
   */
  @Unsafe
  @Suppress("UNCHECKED_CAST")
  fun writeStringUnsafe(value: Any): String = writeString(value as T)

  fun writeString(value: T): String
}
