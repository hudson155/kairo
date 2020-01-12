package io.limberapp.backend.module.orgs.store.org

import com.piperframework.store.Store
import io.limberapp.backend.module.orgs.model.org.OrgModel
import java.util.UUID

internal interface OrgStore : Store {

    fun create(model: OrgModel)

    fun get(orgId: UUID): OrgModel?

    fun getByMemberId(memberId: UUID): List<OrgModel>

    fun update(orgId: UUID, update: OrgModel.Update): OrgModel

    fun delete(orgId: UUID)
}
