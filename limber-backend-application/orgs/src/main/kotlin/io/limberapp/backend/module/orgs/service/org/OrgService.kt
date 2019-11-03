package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import java.util.UUID

internal interface OrgService {

    fun create(model: OrgModel.Creation): OrgModel.Complete

    fun get(id: UUID): OrgModel.Complete?

    fun getByMemberId(memberId: UUID): List<OrgModel.Complete>

    fun createMembership(id: UUID, model: MembershipModel.Creation)

    fun deleteMembership(id: UUID, memberId: UUID)

    fun update(id: UUID, model: OrgModel.Update): OrgModel.Complete

    fun delete(id: UUID)
}
