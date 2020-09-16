package io.limberapp.backend.module.auth.client.org.role

import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.common.restInterface.Fetch
import io.limberapp.common.serialization.Json

class OrgRoleMembershipClientImpl(private val fetch: Fetch, private val json: Json) : OrgRoleMembershipClient {
  override suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Post) =
    fetch(endpoint) { json.parse<OrgRoleMembershipRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: OrgRoleMembershipApi.GetByOrgRoleGuid) =
    fetch(endpoint) { json.parseSet<OrgRoleMembershipRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Delete) =
    fetch(endpoint)
}
