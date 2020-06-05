package io.limberapp.web.context.globalState.action.orgRole

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun orgRoleReducer(state: GlobalStateContext, action: OrgRoleAction): GlobalStateContext {
  return when (action) {
    is OrgRoleAction.BeginLoading -> {
      check(!state.orgRoles.hasBegunLoading)
      state.copy(orgRoles = LoadableState.loading())
    }
    is OrgRoleAction.SetValue -> {
      check(state.orgRoles.isLoading)
      state.copy(orgRoles = LoadableState.Loaded(action.orgRoles.associateBy { it.guid }))
    }
    is OrgRoleAction.UpdateValue -> {
      check(state.orgRoles.isLoaded)
      state.copy(orgRoles = state.orgRoles.update { it.orEmpty().plus(action.orgRole.guid to action.orgRole) })
    }
    is OrgRoleAction.DeleteValue -> {
      check(state.orgRoles.isLoaded)
      state.copy(orgRoles = state.orgRoles.update { it.orEmpty().minus(action.orgRoleGuid) })
    }
    is OrgRoleAction.SetError -> {
      state.copy(orgRoles = LoadableState.Error(action.errorMessage))
    }
  }
}
