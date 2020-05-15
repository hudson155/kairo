package io.limberapp.web.context.globalState.action.users

import io.limberapp.web.context.globalState.GlobalStateContext

internal fun usersReducer(state: GlobalStateContext, action: UsersAction): GlobalStateContext {
    return when (action) {
        is UsersAction.BeginLoading -> state.copy(
            users = state.users.loading()
        )
        is UsersAction.SetValue -> state.copy(
            users = state.users.loaded(action.users.associateBy { it.guid })
        )
    }
}
