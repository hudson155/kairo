package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.OrgModel
import java.util.UUID

internal interface OrgService {

    fun create(model: OrgModel.Creation): OrgModel.Complete

    fun getById(id: UUID): OrgModel.Complete?

    fun getByMemberId(memberId: UUID): List<OrgModel.Complete>

    fun update(id: UUID, model: OrgModel.Update): OrgModel.Complete
}
