package io.limberapp.framework.endpoint.authorization

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.Payload
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

sealed class Authorization {

    abstract fun authorize(payload: Payload?, command: AbstractCommand): Boolean
}

object Public : Authorization() {
    override fun authorize(payload: Payload?, command: AbstractCommand) = true
}

object AnyJwt : Authorization() {
    override fun authorize(payload: Payload?, command: AbstractCommand) = payload != null
}

class OrgMember(private val orgId: UUID) : Authorization() {
    override fun authorize(payload: Payload?, command: AbstractCommand): Boolean {
        payload ?: return false
        return try {
            payload.getClaim("org").asMap().containsKey(orgId.toString())
        } catch (e: JWTDecodeException) {
            false
        }
    }
}
