package io.limberapp.web.api.user

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.api.Fetch
import io.limberapp.web.api.json

internal class UserApi(private val fetch: Fetch) {

    suspend fun get(userId: String): UserRep.Complete {
        val string = fetch.get("/users/$userId")
        return json.parse(string)
    }

    suspend fun patch(userId: String, rep: UserRep.Update): UserRep.Complete {
        val string = fetch.patch("/users/$userId", rep)
        return json.parse(string)
    }
}
