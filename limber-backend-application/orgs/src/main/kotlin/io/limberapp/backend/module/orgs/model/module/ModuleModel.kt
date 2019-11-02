package io.limberapp.backend.module.orgs.model.module

import io.limberapp.framework.model.CompleteModel
import io.limberapp.framework.model.CreationModel
import io.limberapp.framework.model.UpdateModel
import java.time.LocalDateTime
import java.util.UUID

internal object ModuleModel {

    enum class Type {
        FORM,
    }

    data class Creation(
        override val id: UUID,
        override val created: LocalDateTime,
        override val version: Int,
        val name: String,
        val type: Type
    ) : CreationModel()

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        override val version: Int,
        val name: String,
        val type: Type
    ) : CompleteModel()

    data class Update(
        val name: String?
    ) : UpdateModel()
}
