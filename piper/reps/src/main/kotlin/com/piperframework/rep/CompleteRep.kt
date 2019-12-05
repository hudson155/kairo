package com.piperframework.rep

import java.time.LocalDateTime
import java.util.UUID

interface CompleteRep {
    val id: UUID
    val created: LocalDateTime
}
