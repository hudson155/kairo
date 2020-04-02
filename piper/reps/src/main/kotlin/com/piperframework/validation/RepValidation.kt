package com.piperframework.validation

import com.piperframework.rep.ValidatedRep
import kotlin.reflect.KProperty1

class RepValidation(validation: Builder.() -> Unit) {

    class Builder internal constructor() {

        val validations = mutableListOf<Pair<ValueValidation<*>, Boolean>>()

        fun <R : ValidatedRep, T : Any?> R.validate(
            property: KProperty1<R, T>,
            validator: ValueValidation<T>.() -> Boolean
        ) {
            val validation = ValueValidation(property.name, property.get(this))
            validations.add(validation to validation.validator())
        }
    }

    private val validations = Builder().apply { validation() }.validations

    val isValid = validations.all { it.second }

    val firstInvalidPropertyName get() = validations.first { !it.second }.first.name
}