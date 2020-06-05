package io.limberapp.web.context.globalState.action.orgRoleMemberships

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun orgRoleMembershipsReducer(
  state: GlobalStateContext,
  action: OrgRoleMembershipsAction
): GlobalStateContext = with(state.orgRoleMemberships) {
  return@with when (action) {
    is OrgRoleMembershipsAction.BeginLoading -> {
      check(get(action.orgRoleGuid) == null)
      state.copy(orgRoleMemberships = plus(action.orgRoleGuid to LoadableState.loading()))
    }
    is OrgRoleMembershipsAction.SetValue -> {
      val orgRoleMembership = checkNotNull(get(action.orgRoleGuid))
      check(orgRoleMembership.isLoading)
      state.copy(
        orgRoleMemberships = plus(
          action.orgRoleGuid to LoadableState.Loaded(action.orgRoleMemberships.associateBy { it.accountGuid })
        )
      )
    }
    is OrgRoleMembershipsAction.UpdateValue -> {
      val orgRoleMembership = checkNotNull(get(action.orgRoleGuid))
      check(orgRoleMembership.isLoaded)
      state.copy(
        orgRoleMemberships = plus(
          action.orgRoleGuid to orgRoleMembership
            .update { it.orEmpty().plus(action.orgRoleMembership.accountGuid to action.orgRoleMembership) }
        )
      )
    }
    is OrgRoleMembershipsAction.DeleteValue -> {
      val orgRoleMembership = checkNotNull(get(action.orgRoleGuid))
      check(orgRoleMembership.isLoaded)
      state.copy(
        orgRoleMemberships = plus(
          action.orgRoleGuid to orgRoleMembership.update { it.orEmpty().minus(action.accountGuid) }
        )
      )
    }
    is OrgRoleMembershipsAction.SetError -> {
      state.copy(orgRoleMemberships = plus(action.orgRoleGuid to LoadableState.Error(action.errorMessage)))
    }
  }
}
