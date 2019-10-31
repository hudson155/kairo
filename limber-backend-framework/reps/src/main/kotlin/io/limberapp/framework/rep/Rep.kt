package io.limberapp.framework.rep

import io.limberapp.framework.validation.validation.Validation
import java.time.LocalDateTime
import java.util.UUID
import kotlin.reflect.KProperty1

sealed class Rep

sealed class ValidatedRep : Rep() {

    abstract fun validate()

    fun <R : ValidatedRep, T : Any?> R.validate(
        property: KProperty1<R, T>,
        validator: Validation<T>.() -> Unit
    ) {
        Validation(property.get(this), property.name).apply(validator)
    }
}

abstract class CreationRep : ValidatedRep()

abstract class CompleteRep : Rep() {
    abstract val id: UUID
    abstract val created: LocalDateTime
    abstract val version: Int
}

abstract class UpdateRep : ValidatedRep()
