package io.limberapp.web.context.globalState.action.users

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.context.globalState.action.org.state
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal sealed class UsersAction : Action() {
  internal object BeginLoading : UsersAction()

  internal data class SetValue(val users: Set<UserRep.Summary>) : UsersAction()

  internal data class SetError(val errorMessage: String?) : UsersAction()
}

internal fun ComponentWithApi.loadUsers() {
  val orgGuid = gs.org.state.guid

  useEffect(listOf(orgGuid)) {
    if (gs.users.hasBegunLoading) return@useEffect
    dispatch(UsersAction.BeginLoading)
    async {
      api.users(UserApi.GetByOrgGuid(orgGuid)).fold(
        onSuccess = { users -> dispatch(UsersAction.SetValue(users)) },
        onFailure = { dispatch(UsersAction.SetError(it.message)) }
      )
    }
  }
}
