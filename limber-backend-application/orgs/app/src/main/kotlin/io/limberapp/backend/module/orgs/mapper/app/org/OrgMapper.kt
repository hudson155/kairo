package io.limberapp.backend.module.orgs.mapper.app.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.backend.module.orgs.mapper.app.membership.MembershipMapper
import io.limberapp.backend.module.orgs.model.org.OrgModel

internal class OrgMapper @Inject constructor(
    private val membershipMapper: MembershipMapper
) {

    fun entity(model: OrgModel.Creation) = OrgEntity.Complete(
        id = model.id,
        created = model.created,
        version = model.version,
        name = model.name,
        members = model.members.map { membershipMapper.entity(it) }
    )

    fun completeModel(entity: OrgEntity.Complete) = OrgModel.Complete(
        id = entity.id,
        created = entity.created,
        version = entity.version,
        name = entity.name,
        members = entity.members.map { membershipMapper.completeModel(it) }
    )

    fun update(model: OrgModel.Update) = OrgEntity.Update(
        name = model.name
    )
}
