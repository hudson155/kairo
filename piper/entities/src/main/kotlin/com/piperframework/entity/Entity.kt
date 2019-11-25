@file:Suppress("UnnecessaryAbstractClass")

package com.piperframework.entity

import java.time.LocalDateTime
import java.util.UUID

abstract class CompleteSubentity {
    abstract val created: LocalDateTime
}

abstract class CompleteEntity {
    abstract val id: UUID
    abstract val created: LocalDateTime
    abstract val version: Int
}

abstract class UpdateEntity
