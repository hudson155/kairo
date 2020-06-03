package io.limberapp.backend.module.users.client.user

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep

class UserClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: UserApi.Get): UserRep.Complete {
    val string = fetch(endpoint) { it }.getOrThrow()
    return json.parse(string)
  }

  suspend operator fun invoke(endpoint: UserApi.GetByOrgGuid): Set<UserRep.Summary> {
    val string = fetch(endpoint) { it }.getOrThrow()
    return json.parseSet(string)
  }

  suspend operator fun invoke(endpoint: UserApi.Patch): UserRep.Complete {
    val string = fetch(endpoint) { it }.getOrThrow()
    return json.parse(string)
  }
}
