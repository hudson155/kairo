package io.limberapp.backend.module.auth.client.org.role

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep

class OrgRoleClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: OrgRoleApi.Post) =
    fetch(endpoint) { json.parse<OrgRoleRep.Complete>(it) }

  suspend operator fun invoke(endpoint: OrgRoleApi.GetByOrgGuid) =
    fetch(endpoint) { json.parseSet<OrgRoleRep.Complete>(it) }

  suspend operator fun invoke(endpoint: OrgRoleApi.Patch) =
    fetch(endpoint) { json.parse<OrgRoleRep.Complete>(it) }

  suspend operator fun invoke(endpoint: OrgRoleApi.Delete) =
    fetch(endpoint)
}
