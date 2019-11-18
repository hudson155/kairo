package io.limberapp.backend.module.orgs.model.org

import io.limberapp.framework.model.CreationModel
import io.limberapp.framework.model.UpdateModel
import java.time.LocalDateTime
import java.util.UUID

data class OrgModel(
    override val id: UUID,
    override val created: LocalDateTime,
    override val version: Int,
    val name: String,
    val members: List<MembershipModel>
) : CreationModel() {

    data class Update(
        val name: String?
    ) : UpdateModel()
}
