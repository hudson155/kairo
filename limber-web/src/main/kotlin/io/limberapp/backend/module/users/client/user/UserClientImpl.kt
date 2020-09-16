package io.limberapp.backend.module.users.client.user

import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.common.restInterface.Fetch
import io.limberapp.common.serialization.Json

class UserClientImpl(private val fetch: Fetch, private val json: Json) : UserClient {
  override suspend operator fun invoke(endpoint: UserApi.Get) =
    fetch(endpoint) { json.parse<UserRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: UserApi.GetByOrgGuid) =
    fetch(endpoint) { json.parseSet<UserRep.Summary>(it) }

  override suspend operator fun invoke(endpoint: UserApi.Patch) =
    fetch(endpoint) { json.parse<UserRep.Complete>(it) }
}
