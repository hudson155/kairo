package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun tenantReducer(state: GlobalStateContext, action: TenantAction): GlobalStateContext {
  return when (action) {
    is TenantAction.BeginLoading -> state.copy(
      tenant = LoadableState.loading()
    )
    is TenantAction.SetValue -> state.copy(
      tenant = LoadableState.Loaded(action.tenant)
    )
    is TenantAction.SetError -> state.copy(
      tenant = LoadableState.Error(action.errorMessage)
    )
  }
}
