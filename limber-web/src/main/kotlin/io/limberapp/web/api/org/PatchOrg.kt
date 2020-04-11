package io.limberapp.web.api.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.api.Api

internal suspend fun patchOrg(orgId: String, rep: OrgRep.Update): OrgRep.Complete {
    return Api.patch("/orgs/$orgId", rep)
        .unsafeCast<OrgRep.Complete>()
}
