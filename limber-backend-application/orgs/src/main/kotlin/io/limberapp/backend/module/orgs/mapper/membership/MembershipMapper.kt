package io.limberapp.backend.module.orgs.mapper.membership

import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep

internal object MembershipMapper {

    fun completeRep(model: MembershipModel.Complete) = MembershipRep.Complete(
        created = model.created,
        userId = model.userId,
        userName = model.userName
    )
}
