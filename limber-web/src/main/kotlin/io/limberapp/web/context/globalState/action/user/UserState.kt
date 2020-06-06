package io.limberapp.web.context.globalState.action.user

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.LoadableState

internal typealias UserState = LoadableState<UserRep.Complete>

/**
 * [UserState] is loaded eagerly, so it's ok to access [state] all the time without doing a null check.
 */
internal val LoadableState<UserRep.Complete>.state get() = checkNotNull(stateOrNull)
