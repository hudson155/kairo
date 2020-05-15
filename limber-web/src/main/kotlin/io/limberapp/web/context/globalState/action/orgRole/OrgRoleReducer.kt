package io.limberapp.web.context.globalState.action.orgRole

import io.limberapp.web.context.globalState.GlobalStateContext

internal fun orgRoleReducer(state: GlobalStateContext, action: OrgRoleAction): GlobalStateContext {
  return when (action) {
    is OrgRoleAction.BeginLoading -> state.copy(
      orgRoles = state.orgRoles.loading()
    )
    is OrgRoleAction.SetValue -> state.copy(
      orgRoles = state.orgRoles.loaded(action.orgRoles.associateBy { it.guid })
    )
    is OrgRoleAction.UpdateValue -> state.copy(
      orgRoles = state.orgRoles.update { it.orEmpty().plus(action.orgRole.guid to action.orgRole) }
    )
  }
}
