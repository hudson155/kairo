package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.web.context.globalState.GlobalStateContext

internal fun tenantReducer(state: GlobalStateContext, action: TenantAction): GlobalStateContext {
  return when (action) {
    is TenantAction.BeginLoading -> state.copy(
      tenant = state.tenant.loading()
    )
    is TenantAction.SetValue -> state.copy(
      tenant = state.tenant.loaded(action.tenant)
    )
  }
}
