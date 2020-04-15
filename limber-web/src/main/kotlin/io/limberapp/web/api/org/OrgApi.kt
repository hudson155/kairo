package io.limberapp.web.api.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.api.Fetch

internal class OrgApi(private val fetch: Fetch) {

    suspend fun get(orgId: String): OrgRep.Complete {
        return fetch.get("/orgs/$orgId")
            .unsafeCast<OrgRep.Complete>()
    }

    suspend fun patch(orgId: String, rep: OrgRep.Update): OrgRep.Complete {
        return fetch.patch("/orgs/$orgId", rep)
            .unsafeCast<OrgRep.Complete>()
    }
}
