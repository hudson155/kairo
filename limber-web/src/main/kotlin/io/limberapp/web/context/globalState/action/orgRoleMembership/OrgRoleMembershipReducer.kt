package io.limberapp.web.context.globalState.action.orgRoleMembership

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun orgRoleMembershipReducer(state: GlobalStateContext, action: OrgRoleMembershipAction): GlobalStateContext {
  return when (action) {
    is OrgRoleMembershipAction.BeginLoading -> state.copy(
      orgRoleMemberships = state.orgRoleMemberships.plus(action.orgRoleGuid to LoadableState.loading())
    )
    is OrgRoleMembershipAction.SetValue -> state.copy(
      orgRoleMemberships = state.orgRoleMemberships.plus(
        action.orgRoleGuid to LoadableState.Loaded(action.orgRoleMemberships.associateBy { it.accountGuid })
      )
    )
    is OrgRoleMembershipAction.UpdateValue -> state.copy(
      orgRoleMemberships = state.orgRoleMemberships.plus(
        action.orgRoleGuid to checkNotNull(state.orgRoleMemberships[action.orgRoleGuid])
          .update { it.orEmpty().plus(action.orgRoleMembership.accountGuid to action.orgRoleMembership) }
      )
    )
    is OrgRoleMembershipAction.DeleteValue -> state.copy(
      orgRoleMemberships = state.orgRoleMemberships.plus(
        action.orgRoleGuid to checkNotNull(state.orgRoleMemberships[action.orgRoleGuid])
          .update { it.orEmpty().minus(action.accountGuid) }
      )
    )
    is OrgRoleMembershipAction.SetError -> state.copy(
      orgRoleMemberships = state.orgRoleMemberships.plus(
        action.orgRoleGuid to LoadableState.Error(action.errorMessage)
      )
    )
  }
}
