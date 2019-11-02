package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.framework.store.Store
import java.util.UUID

internal interface OrgStore :
    Store<OrgModel.Creation, OrgModel.Complete, OrgModel.Update> {

    fun getByMemberId(memberId: UUID): List<OrgModel.Complete>
}
