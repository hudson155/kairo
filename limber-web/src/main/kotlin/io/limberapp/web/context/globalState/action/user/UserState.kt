package io.limberapp.web.context.globalState.action.user

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.LoadableState

internal typealias UserState = LoadableState<UserRep.Complete>
