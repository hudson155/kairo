package io.limberapp.backend.module.orgs.mapper.api.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.mapper.api.membership.MembershipMapper
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.framework.util.uuidGenerator.UuidGenerator
import java.time.Clock
import java.time.LocalDateTime

internal class OrgMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val membershipMapper: MembershipMapper
) {

    fun model(rep: OrgRep.Creation) = OrgModel(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(clock),
        version = 0,
        name = rep.name,
        members = emptyList()
    )

    fun completeRep(model: OrgModel) = OrgRep.Complete(
        id = model.id,
        created = model.created,
        name = model.name,
        members = model.members.map { membershipMapper.completeRep(it) }
    )

    fun update(rep: OrgRep.Update) = OrgModel.Update(
        name = rep.name
    )
}
