package io.limberapp.web.context.globalState.action.orgRoles

import io.limberapp.web.context.LoadableState

internal fun orgRolesReducer(
  state: OrgRolesState,
  action: OrgRolesAction
): OrgRolesState = with(state) {
  return@with when (action) {
    is OrgRolesAction.BeginLoading -> {
      LoadableState.loading()
    }
    is OrgRolesAction.SetValue -> {
      LoadableState.Loaded(action.orgRoles.associateBy { it.guid })
    }
    is OrgRolesAction.UpdateValue -> {
      check(isLoaded)
      update { it.plus(action.orgRole.guid to action.orgRole) }
    }
    is OrgRolesAction.DeleteValue -> {
      check(isLoaded)
      update { it.minus(action.orgRoleGuid) }
    }
    is OrgRolesAction.SetError -> {
      LoadableState.Error(action.errorMessage)
    }
  }
}
