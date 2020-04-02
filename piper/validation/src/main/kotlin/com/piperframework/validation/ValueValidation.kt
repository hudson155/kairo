package com.piperframework.validation

/**
 * The Validation class provides utilities for parameter validation. They're all stored outside of the class in
 * extension functions, or else this class would get huge.
 */
data class ValueValidation<T>(val name: String, val value: T)

fun <T : Any> ValueValidation<T?>.ifPresent(validator: ValueValidation<T>.() -> Boolean): Boolean {
    if (value == null) return true
    return ValueValidation(name, value).validator()
}
