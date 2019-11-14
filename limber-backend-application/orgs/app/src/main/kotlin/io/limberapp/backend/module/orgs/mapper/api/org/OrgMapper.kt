package io.limberapp.backend.module.orgs.mapper.api.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import io.limberapp.backend.module.orgs.mapper.api.membership.MembershipMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.framework.util.uuidGenerator.UuidGenerator
import java.time.Clock
import java.time.LocalDateTime

internal class OrgMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val membershipMapper: MembershipMapper
) {

    fun creationEntity(rep: OrgRep.Creation) = OrgEntity.Creation(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(clock),
        version = 0,
        name = rep.name,
        members = emptyList()
    )

    fun completeRep(entity: OrgEntity.Complete) = OrgRep.Complete(
        id = entity.id,
        created = entity.created,
        name = entity.name,
        members = entity.members.map { membershipMapper.completeRep(it) }
    )

    fun updateEntity(rep: OrgRep.Update) = OrgEntity.Update(
        name = rep.name
    )
}
