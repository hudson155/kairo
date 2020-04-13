package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun tenantReducer(state: GlobalStateContext, action: TenantAction): GlobalStateContext {
    return when (action) {
        is BeginLoadingTenant -> state.copy(
            tenant = state.tenant.copy(
                loadingStatus = LoadableState.LoadingStatus.LOADING
            )
        )
        is SetTenant -> state.copy(
            tenant = state.tenant.copy(
                loadingStatus = LoadableState.LoadingStatus.LOADED,
                state = action.tenant
            )
        )
    }
}
