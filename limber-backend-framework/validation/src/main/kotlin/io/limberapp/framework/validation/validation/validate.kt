package io.limberapp.framework.validation.validation

import io.limberapp.framework.model.PartialModel
import kotlin.reflect.KProperty1

fun <M : PartialModel, T : PartialModel> M.validate(
    property: KProperty1<M, List<T>>
) {
    validate(property) { subject.forEach { it.validate() } }
}

fun <M : PartialModel, T : Any?> M.validate(
    property: KProperty1<M, T>,
    validator: Validation<T>.() -> Unit
) {
    Validation(property.get(this), property.name).apply(validator)
}
