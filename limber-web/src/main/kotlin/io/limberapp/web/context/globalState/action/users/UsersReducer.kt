package io.limberapp.web.context.globalState.action.users

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun usersReducer(state: GlobalStateContext, action: UsersAction): GlobalStateContext {
  return when (action) {
    is UsersAction.BeginLoading -> state.copy(
      users = LoadableState.loading()
    )
    is UsersAction.SetValue -> state.copy(
      users = LoadableState.Loaded(action.users.associateBy { it.guid })
    )
    is UsersAction.SetError -> state.copy(
      users = LoadableState.Error(action.errorMessage)
    )
  }
}
