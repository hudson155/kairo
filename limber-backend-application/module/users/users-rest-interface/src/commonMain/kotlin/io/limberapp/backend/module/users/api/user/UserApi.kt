package io.limberapp.backend.module.users.api.user

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.users.rep.account.UserRep

@Suppress("StringLiteralDuplication")
object UserApi {
    data class Post(val rep: UserRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/users",
        body = rep
    )

    data class Get(val userId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/users/${enc(userId)}"
    )

    data class GetByEmailAddress(val emailAddress: String) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/users",
        queryParams = listOf("emailAddress" to enc(emailAddress))
    )

    data class Patch(val userId: UUID, val rep: UserRep.Update?) : PiperEndpoint(
        httpMethod = HttpMethod.PATCH,
        path = "/users/${enc(userId)}",
        body = rep
    )

    data class Delete(val userId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/users/${enc(userId)}"
    )
}
