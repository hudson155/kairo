package io.limberapp.web.context.globalState.action.user

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext
import io.limberapp.web.context.globalState.warnIfDoubleLoading

internal fun userReducer(
  state: GlobalStateContext,
  action: UserAction
): GlobalStateContext = with(state.user) {
  return@with when (action) {
    is UserAction.BeginLoading -> {
      warnIfDoubleLoading(GlobalStateContext::user)
      state.copy(user = LoadableState.loading())
    }
    is UserAction.SetValue -> {
      check(isLoading)
      state.copy(user = LoadableState.Loaded(action.user))
    }
    is UserAction.SetError -> {
      state.copy(user = LoadableState.Error(action.errorMessage))
    }
  }
}
