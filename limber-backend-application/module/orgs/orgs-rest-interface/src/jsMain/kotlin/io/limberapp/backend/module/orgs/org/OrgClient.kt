package io.limberapp.backend.module.orgs.org

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep

class OrgClient(private val fetch: Fetch, private val json: Json) {

    suspend operator fun invoke(endpoint: OrgApi.Get): OrgRep.Complete {
        val string = fetch(endpoint)
        return json.parse(string)
    }

    suspend operator fun invoke(endpoint: OrgApi.Patch): OrgRep.Complete {
        val string = fetch(endpoint)
        return json.parse(string)
    }
}
