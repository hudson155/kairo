package io.limberapp.web.context.globalState.action.user

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun userReducer(state: GlobalStateContext, action: UserAction): GlobalStateContext {
  return when (action) {
    is UserAction.BeginLoading -> {
      check(!state.user.hasBegunLoading)
      state.copy(user = LoadableState.loading())
    }
    is UserAction.SetValue -> {
      check(state.user.isLoading)
      state.copy(user = LoadableState.Loaded(action.user))
    }
    is UserAction.SetError -> {
      state.copy(user = LoadableState.Error(action.errorMessage))
    }
  }
}
