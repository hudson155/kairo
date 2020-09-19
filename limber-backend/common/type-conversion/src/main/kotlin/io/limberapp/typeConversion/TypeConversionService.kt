package io.limberapp.typeConversion

import kotlin.reflect.KClass

interface TypeConversionService<T : Any> {
  val kClass: KClass<T>

  fun isValid(value: String): Boolean

  fun fromString(value: String): T

  fun toString(value: T): String
}
