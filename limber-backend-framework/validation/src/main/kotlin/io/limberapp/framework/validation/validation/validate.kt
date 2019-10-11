package io.limberapp.framework.validation.validation

import io.limberapp.framework.rep.Rep
import kotlin.reflect.KProperty1

fun <R : Rep, T : Any?> R.validate(
    property: KProperty1<R, T>,
    validator: Validation<T>.() -> Unit
) {
    Validation(property.get(this), property.name).apply(validator)
}
