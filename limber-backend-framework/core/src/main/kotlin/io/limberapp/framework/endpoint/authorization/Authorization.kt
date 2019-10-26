package io.limberapp.framework.endpoint.authorization

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
            return payload.user.id == userId
        }
    }

    class OrgMember(private val orgId: UUID) : Authorization() {
        override fun authorize(payload: Jwt?, command: AbstractCommand): Boolean {
            payload ?: return false
            return payload.orgs.containsKey(orgId)
        }
    }
}
