package io.limberapp.web.api.user

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.api.Api

internal suspend fun patchUser(userId: String, rep: UserRep.Update): UserRep.Complete {
    return Api.patch("/users/$userId", rep)
        .unsafeCast<UserRep.Complete>()
}
