package io.limberapp.web.context.globalState

import io.limberapp.web.context.globalState.action.org.OrgState
import io.limberapp.web.context.globalState.action.orgRole.OrgRoleState
import io.limberapp.web.context.globalState.action.orgRoleMembership.OrgRoleMembershipState
import io.limberapp.web.context.globalState.action.tenant.TenantState
import io.limberapp.web.context.globalState.action.user.UserState
import io.limberapp.web.context.globalState.action.users.UsersState

internal data class GlobalStateContext(
  val org: OrgState,
  val orgRoleMemberships: OrgRoleMembershipState,
  val orgRoles: OrgRoleState,
  val tenant: TenantState,
  val user: UserState,
  val users: UsersState
)
