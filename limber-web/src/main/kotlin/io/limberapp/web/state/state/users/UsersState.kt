package io.limberapp.web.state.state.users

import io.limberapp.common.types.UUID
import io.limberapp.backend.module.users.rep.account.UserRep

private typealias AccountGuid = UUID
internal typealias UsersState = Map<AccountGuid, UserRep.Summary>
