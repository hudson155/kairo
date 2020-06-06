package io.limberapp.web.context.globalState.action.orgRoles

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext
import io.limberapp.web.context.globalState.warnIfDoubleLoading

internal fun orgRolesReducer(
  state: GlobalStateContext,
  action: OrgRolesAction
): GlobalStateContext = with(state.orgRoles) {
  return@with when (action) {
    is OrgRolesAction.BeginLoading -> {
      warnIfDoubleLoading(GlobalStateContext::orgRoles)
      state.copy(orgRoles = LoadableState.loading())
    }
    is OrgRolesAction.SetValue -> {
      check(isLoading)
      state.copy(orgRoles = LoadableState.Loaded(action.orgRoles.associateBy { it.guid }))
    }
    is OrgRolesAction.UpdateValue -> {
      check(isLoaded)
      state.copy(orgRoles = update { it.orEmpty().plus(action.orgRole.guid to action.orgRole) })
    }
    is OrgRolesAction.DeleteValue -> {
      check(isLoaded)
      state.copy(orgRoles = update { it.orEmpty().minus(action.orgRoleGuid) })
    }
    is OrgRolesAction.SetError -> {
      state.copy(orgRoles = LoadableState.Error(action.errorMessage))
    }
  }
}
