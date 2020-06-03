package io.limberapp.web.context.globalState.action.user

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun userReducer(state: GlobalStateContext, action: UserAction): GlobalStateContext {
  return when (action) {
    is UserAction.BeginLoading -> state.copy(
      user = LoadableState.loading()
    )
    is UserAction.SetValue -> state.copy(
      user = LoadableState.Loaded(action.user)
    )
  }
}
