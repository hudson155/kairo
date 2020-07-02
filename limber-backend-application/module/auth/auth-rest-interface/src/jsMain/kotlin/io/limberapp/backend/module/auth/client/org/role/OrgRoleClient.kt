package io.limberapp.backend.module.auth.client.org.role

import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep

interface OrgRoleClient {
  suspend operator fun invoke(endpoint: OrgRoleApi.Post): Result<OrgRoleRep.Complete>

  suspend operator fun invoke(endpoint: OrgRoleApi.GetByOrgGuid): Result<Set<OrgRoleRep.Complete>>

  suspend operator fun invoke(endpoint: OrgRoleApi.Patch): Result<OrgRoleRep.Complete>

  suspend operator fun invoke(endpoint: OrgRoleApi.Delete): Result<Unit>
}
