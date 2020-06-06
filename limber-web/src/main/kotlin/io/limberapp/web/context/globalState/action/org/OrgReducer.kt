package io.limberapp.web.context.globalState.action.org

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext
import io.limberapp.web.context.globalState.warnIfDoubleLoading

internal fun orgReducer(
  state: GlobalStateContext,
  action: OrgAction
): GlobalStateContext = with(state.org) {
  return@with when (action) {
    is OrgAction.BeginLoading -> {
      warnIfDoubleLoading(GlobalStateContext::org)
      state.copy(org = LoadableState.loading())
    }
    is OrgAction.SetValue -> {
      check(isLoading)
      state.copy(org = LoadableState.Loaded(action.org))
    }
    is OrgAction.SetError -> {
      state.copy(org = LoadableState.Error(action.errorMessage))
    }
  }
}
