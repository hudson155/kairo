package io.limberapp.web.state.state.user

import com.piperframework.types.UUID
import com.piperframework.util.Outcome
import io.limberapp.backend.module.users.rep.account.UserRep

internal interface UserMutator {
  suspend fun patch(userGuid: UUID, rep: UserRep.Update): Outcome<Unit>
}
