package io.limberapp.backend.module.orgs.mapper.app.membership

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.model.org.MembershipModel

internal class MembershipMapper @Inject constructor() {

    fun entity(model: MembershipModel.Creation) = MembershipEntity.Complete(
        created = model.created,
        userId = model.userId
    )

    fun completeModel(entity: MembershipEntity.Complete) = MembershipModel.Complete(
        created = entity.created,
        userId = entity.userId
    )
}
