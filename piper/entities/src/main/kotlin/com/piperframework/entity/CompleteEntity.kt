package com.piperframework.entity

import java.time.LocalDateTime
import java.util.UUID

interface CompleteEntity {
    val id: UUID
    val created: LocalDateTime
}
