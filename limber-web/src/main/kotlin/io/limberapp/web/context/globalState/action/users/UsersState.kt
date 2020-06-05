package io.limberapp.web.context.globalState.action.users

import com.piperframework.types.UUID
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.LoadableState

internal typealias UsersState = LoadableState<Map<UUID, UserRep.Summary>>
