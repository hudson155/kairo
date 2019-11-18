package io.limberapp.backend.module.orgs.mapper.api.membership

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import java.time.Clock
import java.time.LocalDateTime

internal class MembershipMapper @Inject constructor(
    private val clock: Clock
) {

    fun model(rep: MembershipRep.Creation) = MembershipModel(
        created = LocalDateTime.now(clock),
        userId = rep.userId
    )

    fun completeRep(model: MembershipModel) = MembershipRep.Complete(
        created = model.created,
        userId = model.userId
    )
}
