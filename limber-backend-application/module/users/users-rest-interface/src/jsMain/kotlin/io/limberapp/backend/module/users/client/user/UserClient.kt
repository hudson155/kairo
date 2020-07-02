package io.limberapp.backend.module.users.client.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep

interface UserClient {
  suspend operator fun invoke(endpoint: UserApi.Get): Result<UserRep.Complete>

  suspend operator fun invoke(endpoint: UserApi.GetByOrgGuid): Result<Set<UserRep.Summary>>

  suspend operator fun invoke(endpoint: UserApi.Patch): Result<UserRep.Complete>
}
