package com.piperframework.validation.util

import com.piperframework.validation.Validation

fun <T : Any> Validation<T?>.ifPresent(validator: Validation<T>.() -> Unit) {
    if (subject != null) Validation(this.subject, this.name).apply(validator)
}
