package io.limberapp.web.context.globalState.action.user

import com.piperframework.types.UUID
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal sealed class UserAction : Action() {
  internal object BeginLoading : UserAction()

  internal data class SetValue(val user: UserRep.Complete) : UserAction()

  internal data class SetError(val errorMessage: String?) : UserAction()
}

private typealias State = LoadableState<UserRep.Complete>

internal fun ComponentWithApi.load(@Suppress("UNUSED_PARAMETER") state: State, userGuid: UUID) {
  useEffect(listOf(userGuid)) {
    if (gs.user.hasBegunLoading) return@useEffect
    dispatch(UserAction.BeginLoading)
    async {
      api.users(UserApi.Get(userGuid)).fold(
        onSuccess = { user -> dispatch(UserAction.SetValue(user)) },
        onFailure = { dispatch(UserAction.SetError(it.message)) }
      )
    }
  }
}
