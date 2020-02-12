package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.service.org.MembershipService
import java.util.UUID

interface MembershipStore : MembershipService {

    fun create(orgId: UUID, models: Set<MembershipModel>)

    fun get(orgId: UUID, userId: UUID): MembershipModel?

    fun getByOrgId(orgId: UUID): Set<MembershipModel>
}
