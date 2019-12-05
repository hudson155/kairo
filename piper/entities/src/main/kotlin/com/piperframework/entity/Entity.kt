@file:Suppress("UnnecessaryAbstractClass")

package com.piperframework.entity

import java.time.LocalDateTime
import java.util.UUID

interface CompleteSubentity {
    val created: LocalDateTime
}

interface CompleteEntity {
    val id: UUID
    val created: LocalDateTime
}

interface UpdateEntity
