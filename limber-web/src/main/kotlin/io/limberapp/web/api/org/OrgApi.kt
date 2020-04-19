package io.limberapp.web.api.org

import com.piperframework.types.UUID
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.api.Fetch
import io.limberapp.web.api.json

internal class OrgApi(private val fetch: Fetch) {

    suspend fun get(orgId: UUID): OrgRep.Complete {
        val string = fetch.get("/orgs/$orgId")
        return json.parse(string)
    }

    suspend fun patch(orgId: String, rep: OrgRep.Update): OrgRep.Complete {
        val string = fetch.patch("/orgs/$orgId", rep)
        return json.parse(string)
    }
}
