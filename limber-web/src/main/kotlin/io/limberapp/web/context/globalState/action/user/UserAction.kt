package io.limberapp.web.context.globalState.action.user

import com.piperframework.types.UUID
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.EnsureLoadedContext
import io.limberapp.web.util.async
import react.useEffect

internal sealed class UserAction : Action() {
    internal object BeginLoading : UserAction()

    internal data class SetValue(val user: UserRep.Complete) : UserAction()
}

internal fun EnsureLoadedContext.ensureUserLoaded(userGuid: UUID) {
    useEffect(listOf(userGuid)) {
        if (global.state.user.hasBegunLoading) return@useEffect
        global.dispatch(UserAction.BeginLoading)
        async {
            val user = api.users(UserApi.Get(userGuid))
            global.dispatch(UserAction.SetValue(user))
        }
    }
}
