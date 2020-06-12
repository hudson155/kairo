package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.web.context.LoadableState

internal fun tenantReducer(
  state: TenantState,
  action: TenantAction
): TenantState = with(state) {
  return@with when (action) {
    is TenantAction.BeginLoading -> LoadableState.loading()
    is TenantAction.SetValue -> LoadableState.Loaded(action.tenant)
    is TenantAction.SetError -> LoadableState.Error(action.errorMessage)
  }
}
