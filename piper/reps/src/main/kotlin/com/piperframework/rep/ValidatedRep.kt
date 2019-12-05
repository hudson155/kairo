package com.piperframework.rep

import com.piperframework.validation.Validation
import kotlin.reflect.KProperty1

interface ValidatedRep {

    fun validate()

    fun <R : ValidatedRep, T : Any?> R.validate(property: KProperty1<R, T>, validator: Validation<T>.() -> Unit) {
        Validation(property.get(this), property.name).apply(validator)
    }
}
