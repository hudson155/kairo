package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import java.util.UUID

interface OrgService {

    fun create(model: OrgModel)

    fun get(id: UUID): OrgModel?

    fun getByMemberId(memberId: UUID): List<OrgModel>

    fun update(id: UUID, model: OrgModel.Update): OrgModel

    fun createMembership(id: UUID, model: MembershipModel)

    fun deleteMembership(id: UUID, memberId: UUID)

    fun delete(id: UUID)
}
