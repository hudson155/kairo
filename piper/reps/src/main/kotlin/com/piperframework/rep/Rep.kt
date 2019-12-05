@file:Suppress("UnnecessaryAbstractClass")

package com.piperframework.rep

import com.piperframework.validation.Validation
import java.time.LocalDateTime
import java.util.UUID
import kotlin.reflect.KProperty1

interface ValidatedRep {

    fun validate()

    fun <R : ValidatedRep, T : Any?> R.validate(property: KProperty1<R, T>, validator: Validation<T>.() -> Unit) {
        Validation(property.get(this), property.name).apply(validator)
    }
}

interface CreationSubrep : ValidatedRep

interface CreationRep : ValidatedRep

interface CompleteSubrep

interface CompleteRep {
    val id: UUID
    val created: LocalDateTime
}

interface UpdateSubrep : ValidatedRep

interface UpdateRep : ValidatedRep
