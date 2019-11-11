package io.limberapp.framework.rep

import io.limberapp.framework.validation.Validation
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

abstract class CompleteRepWithoutId : Rep() {
    abstract val created: LocalDateTime
}

abstract class CompleteRep : CompleteRepWithoutId() {
    abstract val id: UUID
    abstract override val created: LocalDateTime
}

abstract class UpdateRep : ValidatedRep()
