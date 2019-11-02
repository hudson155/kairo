package io.limberapp.framework.model

import java.time.LocalDateTime
import java.util.UUID

sealed class Model

abstract class CreationModel : Model() {
    abstract val id: UUID
    abstract val created: LocalDateTime
    abstract val version: Int
}

abstract class CompleteModel : Model() {
    abstract val id: UUID
    abstract val created: LocalDateTime
    abstract val version: Int
}

abstract class UpdateModel : Model()
