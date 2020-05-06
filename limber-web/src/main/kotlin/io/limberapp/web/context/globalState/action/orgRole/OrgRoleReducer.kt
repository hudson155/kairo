package io.limberapp.web.context.globalState.action.orgRole

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun orgRoleReducer(state: GlobalStateContext, action: OrgRoleAction): GlobalStateContext {
    return when (action) {
        is OrgRoleAction.BeginLoading -> state.copy(
            orgRoles = state.orgRoles.copy(
                loadingStatus = LoadableState.LoadingStatus.LOADING
            )
        )
        is OrgRoleAction.SetValue -> state.copy(
            orgRoles = state.orgRoles.copy(
                loadingStatus = LoadableState.LoadingStatus.LOADED,
                state = action.orgRoles.associateBy { it.guid }
            )
        )
    }
}
