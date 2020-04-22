package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.OrgModel
import java.util.UUID

interface OrgService {

    fun create(model: OrgModel)

    fun get(orgId: UUID): OrgModel?

    fun getByOwnerAccountId(ownerAccountId: UUID): Set<OrgModel>

    fun update(orgId: UUID, update: OrgModel.Update): OrgModel

    fun delete(orgId: UUID)
}
