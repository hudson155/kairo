package io.limberapp.web.api.user

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.api.Fetch

internal object UserApi {

    suspend fun get(userId: String): UserRep.Complete {
        return Fetch.get("/users/$userId")
            .unsafeCast<UserRep.Complete>()
    }

    suspend fun patch(userId: String, rep: UserRep.Update): UserRep.Complete {
        return Fetch.patch("/users/$userId", rep)
            .unsafeCast<UserRep.Complete>()
    }
}
