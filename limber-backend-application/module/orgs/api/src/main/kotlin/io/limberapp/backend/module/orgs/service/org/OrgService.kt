package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.OrgModel
import java.util.UUID

interface OrgService {

    fun create(model: OrgModel)

    fun get(id: UUID): OrgModel?

    fun getByMemberId(memberId: UUID): List<OrgModel>

    fun update(id: UUID, update: OrgModel.Update): OrgModel

    fun delete(id: UUID)
}
