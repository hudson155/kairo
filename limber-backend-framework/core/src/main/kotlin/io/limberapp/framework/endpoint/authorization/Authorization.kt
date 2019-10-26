package io.limberapp.framework.endpoint.authorization

import com.auth0.jwt.exceptions.JWTDecodeException
import io.limberapp.framework.endpoint.authorization.jwt.Jwt
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

sealed class Authorization {

    abstract fun authorize(payload: Jwt?, command: AbstractCommand): Boolean

    object Public : Authorization() {
        override fun authorize(payload: Jwt?, command: AbstractCommand) = true
    }

    object AnyJwt : Authorization() {
        override fun authorize(payload: Jwt?, command: AbstractCommand) = payload != null
    }

    class User(private val userId: UUID) : Authorization() {
        override fun authorize(payload: Jwt?, command: AbstractCommand): Boolean {
            payload ?: return false
            return try {
                payload.user.id == userId
            } catch (e: JWTDecodeException) {
                false
            }
        }
    }

    class OrgMember(private val orgId: UUID) : Authorization() {
        override fun authorize(payload: Jwt?, command: AbstractCommand): Boolean {
            payload ?: return false
            return try {
                payload.orgs.containsKey(orgId)
            } catch (e: JWTDecodeException) {
                false
            }
        }
    }
}
