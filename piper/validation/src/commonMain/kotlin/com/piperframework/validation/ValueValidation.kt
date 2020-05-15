package com.piperframework.validation

/**
 * Validates a value.
 */
data class ValueValidation<T>(val name: String, val value: T)

/**
 * Only validates the value if it is non-null.
 */
fun <T : Any> ValueValidation<T?>.ifPresent(validator: ValueValidation<T>.() -> Boolean): Boolean {
  if (value == null) return true
  return ValueValidation(name, value).validator()
}
