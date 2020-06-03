package io.limberapp.backend.module.orgs.client.org

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep

class OrgClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: OrgApi.Get) =
    fetch(endpoint) { json.parse<OrgRep.Complete>(it) }

  suspend operator fun invoke(endpoint: OrgApi.Patch) =
    fetch(endpoint) { json.parse<OrgRep.Complete>(it) }
}
