package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.MembershipModel
import java.util.UUID

interface MembershipService {

    fun create(orgId: UUID, model: MembershipModel)

    fun get(orgId: UUID, userId: UUID): MembershipModel?

    fun delete(orgId: UUID, userId: UUID)
}
