package io.limberapp.framework.rep

import java.time.LocalDateTime
import java.util.UUID

sealed class Rep

sealed class ValidatedRep : Rep() {
    abstract fun validate()
}

abstract class CreationRep : ValidatedRep()

abstract class CompleteRep : Rep() {
    abstract val id: UUID
    abstract val created: LocalDateTime
    abstract val version: Int
}

abstract class UpdateRep : ValidatedRep()
