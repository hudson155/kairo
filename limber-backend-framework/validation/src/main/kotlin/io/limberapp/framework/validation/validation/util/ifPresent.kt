package io.limberapp.framework.validation.validation.util

import io.limberapp.framework.validation.validation.Validation

fun <T : Any> Validation<T?>.ifPresent(validator: Validation<T>.() -> Unit) {
    if (subject != null) Validation(this.subject, this.name).apply(validator)
}
