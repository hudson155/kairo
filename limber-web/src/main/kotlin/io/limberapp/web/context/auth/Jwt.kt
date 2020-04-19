package io.limberapp.web.context.auth

import com.piperframework.types.UUID
import io.limberapp.web.util.decodeJwt

internal data class Jwt(val raw: String) {

    internal data class Org(val id: UUID, val name: String)

    internal data class User(val id: UUID, val firstName: String, val lastName: String)

    private val decoded = decodeJwt(raw)

    val org = JSON.parse<Org>(decoded["https://limberapp.io/org"])

    val user = JSON.parse<User>(decoded["https://limberapp.io/user"])
}
