package io.limberapp.web.api.user

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.users.rep.account.UserRep

internal class UserApi(private val fetch: Fetch, private val json: Json) {

    suspend fun get(userId: String): UserRep.Complete {
        val string = fetch.get("/users/$userId")
        return json.parse(string)
    }

    suspend fun patch(userId: String, rep: UserRep.Update): UserRep.Complete {
        val string = fetch.patch("/users/$userId", rep)
        return json.parse(string)
    }
}
