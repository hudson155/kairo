package io.limberapp.web.context.globalState.action.users

import com.piperframework.types.UUID
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.Context
import io.limberapp.web.util.async
import react.*

internal sealed class UsersAction : Action() {
  internal object BeginLoading : UsersAction()

  internal data class SetValue(val users: Set<UserRep.Summary>) : UsersAction()

  internal data class SetError(val errorMessage: String?) : UsersAction()
}

internal fun Context.ensureUsersLoaded(orgGuid: UUID) {
  useEffect(listOf(orgGuid)) {
    if (global.state.users.hasBegunLoading) return@useEffect
    global.dispatch(UsersAction.BeginLoading)
    async {
      api.users(UserApi.GetByOrgGuid(orgGuid)).fold(
        onSuccess = { users -> global.dispatch(UsersAction.SetValue(users)) },
        onFailure = { global.dispatch(UsersAction.SetError(it.message)) }
      )
    }
  }
}
