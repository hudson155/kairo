package io.limberapp.web.api.user

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.api.Api

internal suspend fun getUser(userId: String): UserRep.Complete {
    return Api.get("/users/$userId")
        .unsafeCast<UserRep.Complete>()
}
