package io.limberapp.backend.module.users.client.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.common.util.Outcome

interface UserClient {
  suspend operator fun invoke(endpoint: UserApi.Get): Outcome<UserRep.Complete>

  suspend operator fun invoke(endpoint: UserApi.GetByOrgGuid): Outcome<Set<UserRep.Summary>>

  suspend operator fun invoke(endpoint: UserApi.Patch): Outcome<UserRep.Complete>
}
