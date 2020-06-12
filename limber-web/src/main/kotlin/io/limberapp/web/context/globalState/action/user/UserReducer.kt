package io.limberapp.web.context.globalState.action.user

import io.limberapp.web.context.LoadableState

internal fun userReducer(
  state: UserState,
  action: UserAction
): UserState = with(state) {
  return@with when (action) {
    is UserAction.BeginLoading -> LoadableState.loading()
    is UserAction.SetValue -> LoadableState.Loaded(action.user)
    is UserAction.SetError -> LoadableState.Error(action.errorMessage)
  }
}
