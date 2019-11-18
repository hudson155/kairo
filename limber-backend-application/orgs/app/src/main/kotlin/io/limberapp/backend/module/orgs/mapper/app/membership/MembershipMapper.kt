package io.limberapp.backend.module.orgs.mapper.app.membership

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.model.org.MembershipModel

internal class MembershipMapper @Inject constructor() {

    fun entity(model: MembershipModel) = MembershipEntity(
        created = model.created,
        userId = model.userId
    )

    fun model(entity: MembershipEntity) = MembershipModel(
        created = entity.created,
        userId = entity.userId
    )
}
