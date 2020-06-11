package io.limberapp.web.context.globalState.action.users

import io.limberapp.web.context.LoadableState

internal fun usersReducer(
  state: UsersState,
  action: UsersAction
): UsersState = with(state) {
  return@with when (action) {
    is UsersAction.BeginLoading -> LoadableState.loading()
    is UsersAction.SetValue -> LoadableState.Loaded(action.users.associateBy { it.guid })
    is UsersAction.SetError -> LoadableState.Error(action.errorMessage)
  }
}
