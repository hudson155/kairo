package io.limberapp.framework.model

import java.time.LocalDateTime
import java.util.UUID

sealed class Model

abstract class CreationModelWithoutId : Model() {
    abstract val created: LocalDateTime
}

abstract class CreationModel : CreationModelWithoutId() {
    abstract val id: UUID
    abstract override val created: LocalDateTime
    abstract val version: Int
}

abstract class CompleteModelWithoutId : Model() {
    abstract val created: LocalDateTime
}

abstract class CompleteModel : CompleteModelWithoutId() {
    abstract val id: UUID
    abstract override val created: LocalDateTime
    abstract val version: Int
}

abstract class UpdateModel : Model()
