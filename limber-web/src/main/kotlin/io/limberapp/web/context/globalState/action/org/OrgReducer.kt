package io.limberapp.web.context.globalState.action.org

import io.limberapp.web.context.LoadableState

internal fun orgReducer(
  state: OrgState,
  action: OrgAction
): OrgState = with(state) {
  return@with when (action) {
    is OrgAction.BeginLoading -> LoadableState.loading()
    is OrgAction.SetValue -> LoadableState.Loaded(action.org)
    is OrgAction.SetError -> LoadableState.Error(action.errorMessage)
  }
}
