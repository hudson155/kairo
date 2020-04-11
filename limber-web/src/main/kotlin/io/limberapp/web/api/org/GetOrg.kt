package io.limberapp.web.api.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.api.Api

internal suspend fun getOrg(orgId: String): OrgRep.Complete {
    return Api.get("/orgs/$orgId")
        .unsafeCast<OrgRep.Complete>()
}
