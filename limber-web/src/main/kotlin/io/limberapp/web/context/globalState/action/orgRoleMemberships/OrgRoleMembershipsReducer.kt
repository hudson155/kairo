package io.limberapp.web.context.globalState.action.orgRoleMemberships

import io.limberapp.web.context.LoadableState

internal fun orgRoleMembershipsReducer(
  state: OrgRoleMembershipsState,
  action: OrgRoleMembershipsAction
): OrgRoleMembershipsState = with(state) {
  return@with when (action) {
    is OrgRoleMembershipsAction.BeginLoading -> {
      plus(action.orgRoleGuid to LoadableState.loading())
    }
    is OrgRoleMembershipsAction.SetValue -> {
      plus(action.orgRoleGuid to LoadableState.Loaded(action.orgRoleMemberships.associateBy { it.accountGuid }))
    }
    is OrgRoleMembershipsAction.UpdateValue -> {
      val orgRoleMembership = checkNotNull(get(action.orgRoleGuid))
      check(orgRoleMembership.isLoaded)
      plus(
        action.orgRoleGuid to orgRoleMembership.update {
          it.plus(action.orgRoleMembership.accountGuid to action.orgRoleMembership)
        }
      )
    }
    is OrgRoleMembershipsAction.DeleteValue -> {
      val orgRoleMembership = checkNotNull(get(action.orgRoleGuid))
      check(orgRoleMembership.isLoaded)
      plus(action.orgRoleGuid to orgRoleMembership.update { it.minus(action.accountGuid) })
    }
    is OrgRoleMembershipsAction.SetError -> {
      plus(action.orgRoleGuid to LoadableState.Error(action.errorMessage))
    }
  }
}
