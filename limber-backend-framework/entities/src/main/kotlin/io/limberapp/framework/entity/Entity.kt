@file:Suppress("UnnecessaryAbstractClass")

package io.limberapp.framework.entity

import java.time.LocalDateTime
import java.util.UUID

abstract class CreationSubentity {
    abstract val created: LocalDateTime
}

abstract class CreationEntity {
    abstract val id: UUID
    abstract val created: LocalDateTime
    abstract val version: Int
}

abstract class CompleteSubentity {
    abstract val created: LocalDateTime
}

abstract class CompleteEntity {
    abstract val id: UUID
    abstract val created: LocalDateTime
    abstract val version: Int
}

abstract class UpdateSubentity

abstract class UpdateEntity
