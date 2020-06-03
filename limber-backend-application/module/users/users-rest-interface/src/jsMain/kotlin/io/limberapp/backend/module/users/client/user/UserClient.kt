package io.limberapp.backend.module.users.client.user

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep

class UserClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: UserApi.Get) =
    fetch(endpoint) { json.parse<UserRep.Complete>(it) }

  suspend operator fun invoke(endpoint: UserApi.GetByOrgGuid) =
    fetch(endpoint) { json.parseSet<UserRep.Summary>(it) }

  suspend operator fun invoke(endpoint: UserApi.Patch) =
    fetch(endpoint) { json.parse<UserRep.Complete>(it) }
}
