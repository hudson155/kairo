package io.limberapp.backend.module.auth.client.org.role

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep

class OrgRoleMembershipClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Post) =
    fetch(endpoint) { json.parse<OrgRoleMembershipRep.Complete>(it) }

  suspend operator fun invoke(endpoint: OrgRoleMembershipApi.GetByOrgRoleGuid) =
    fetch(endpoint) { json.parseSet<OrgRoleMembershipRep.Complete>(it) }

  suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Delete) =
    fetch(endpoint)
}
