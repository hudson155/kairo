package io.limberapp.web.context.globalState.action.user

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.globalState.action.Action

internal sealed class UserAction : Action() {
    internal object BeginLoading : UserAction()

    internal data class Set(val user: UserRep.Complete) : UserAction()
}
