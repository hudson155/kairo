package io.limberapp.web.state.state.user

import io.limberapp.common.types.UUID
import io.limberapp.backend.module.users.rep.account.UserRep

internal interface UserMutator {
  suspend fun patch(userGuid: UUID, rep: UserRep.Update): Outcome<Unit>
}
