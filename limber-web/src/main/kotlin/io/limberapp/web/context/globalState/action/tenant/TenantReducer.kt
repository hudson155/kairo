package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun tenantReducer(state: GlobalStateContext, action: TenantAction): GlobalStateContext {
    return when (action) {
        is TenantAction.BeginLoading -> state.copy(
            tenant = state.tenant.copy(
                loadingStatus = LoadableState.LoadingStatus.LOADING
            )
        )
        is TenantAction.Set -> state.copy(
            tenant = state.tenant.copy(
                loadingStatus = LoadableState.LoadingStatus.LOADED,
                state = action.tenant
            )
        )
    }
}
