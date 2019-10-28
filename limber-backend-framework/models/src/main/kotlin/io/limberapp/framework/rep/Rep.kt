package io.limberapp.framework.rep

import java.time.LocalDateTime
import java.util.UUID

interface Rep

interface ValidatedRep : Rep {
    fun validate()
}

interface CreationRep : ValidatedRep

interface CompleteRep : Rep {
    val id: UUID
    val created: LocalDateTime
    val version: Int
}

interface UpdateRep : ValidatedRep
