package com.piperframework.validation

data class ValueValidation<T>(val name: String, val value: T)

fun <T : Any> ValueValidation<T?>.ifPresent(validator: ValueValidation<T>.() -> Boolean): Boolean {
    if (value == null) return true
    return ValueValidation(name, value).validator()
}
