package io.limberapp.backend.module.orgs.mapper.membership

import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import java.time.LocalDateTime

internal object MembershipMapper {

    fun creationModel(rep: MembershipRep.Creation) = MembershipModel.Creation(
        created = LocalDateTime.now(),
        version = 0,
        userId = rep.userId
    )

    fun completeRep(model: MembershipModel.Complete) = MembershipRep.Complete(
        created = model.created,
        userId = model.userId
    )
}
