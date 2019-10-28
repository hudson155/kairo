package io.limberapp.framework.model

import java.time.LocalDateTime
import java.util.UUID

interface Model

interface CreationModel : Model

interface CompleteModel : Model {
    val id: UUID
    val created: LocalDateTime
    val version: Int
}

interface UpdateModel : Model
