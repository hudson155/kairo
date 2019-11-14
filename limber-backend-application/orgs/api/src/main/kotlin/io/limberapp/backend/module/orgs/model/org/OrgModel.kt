package io.limberapp.backend.module.orgs.model.org

import io.limberapp.framework.model.CompleteModel
import io.limberapp.framework.model.CreationModel
import io.limberapp.framework.model.UpdateModel
import java.time.LocalDateTime
import java.util.UUID

object OrgModel {

    data class Creation(
        override val id: UUID,
        override val created: LocalDateTime,
        override val version: Int,
        val name: String,
        val members: List<MembershipModel.Complete>
    ) : CreationModel()

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        override val version: Int,
        val name: String,
        val members: List<MembershipModel.Complete>
    ) : CompleteModel()

    data class Update(
        val name: String?
    ) : UpdateModel()
}
