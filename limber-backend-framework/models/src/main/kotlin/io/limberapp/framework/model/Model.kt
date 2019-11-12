package io.limberapp.framework.model

import java.time.LocalDateTime
import java.util.UUID

abstract class CreationSubmodel {
    abstract val created: LocalDateTime
}

abstract class CreationModel {
    abstract val id: UUID
    abstract val created: LocalDateTime
    abstract val version: Int
}

abstract class CompleteSubmodel {
    abstract val created: LocalDateTime
}

abstract class CompleteModel {
    abstract val id: UUID
    abstract val created: LocalDateTime
    abstract val version: Int
}

abstract class UpdateSubmodel

abstract class UpdateModel
