package io.limberapp.web.context.globalState

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.LoadableState

internal data class GlobalStateContext(
  val org: LoadableState<OrgRep.Complete>,
  val orgRoleMemberships: Map<UUID, LoadableState<Map<UUID, OrgRoleMembershipRep.Complete>>>,
  val orgRoles: LoadableState<Map<UUID, OrgRoleRep.Complete>>,
  val tenant: LoadableState<TenantRep.Complete>,
  val user: LoadableState<UserRep.Complete>,
  val users: LoadableState<Map<UUID, UserRep.Summary>>
)
