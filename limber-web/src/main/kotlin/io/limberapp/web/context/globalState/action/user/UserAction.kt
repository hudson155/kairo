package io.limberapp.web.context.globalState.action.user

import com.piperframework.types.UUID
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.Context
import io.limberapp.web.util.async
import react.*

internal sealed class UserAction : Action() {
  internal object BeginLoading : UserAction()

  internal data class SetValue(val user: UserRep.Complete) : UserAction()

  internal data class SetError(val errorMessage: String?) : UserAction()
}

internal fun Context.ensureUserLoaded(userGuid: UUID) {
  useEffect(listOf(userGuid)) {
    if (global.state.user.hasBegunLoading) return@useEffect
    global.dispatch(UserAction.BeginLoading)
    async {
      api.users(UserApi.Get(userGuid)).fold(
        onSuccess = { user -> global.dispatch(UserAction.SetValue(user)) },
        onFailure = { global.dispatch(UserAction.SetError(it.message)) }
      )
    }
  }
}
