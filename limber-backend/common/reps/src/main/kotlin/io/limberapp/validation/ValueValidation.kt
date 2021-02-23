package io.limberapp.validation

internal data class ValueValidation<T>(val name: String, val value: T, val isValid: Boolean) {
  fun withNamePrefix(prefix: String): ValueValidation<T> = copy(name = prefix + name)
}

fun <T : Any> T?.ifPresent(validator: T.() -> Boolean): Boolean {
  if (this == null) return true
  return validator()
}
