package io.limberapp.backend.module.users.exception.conflict

import com.piperframework.exception.exception.conflict.ConflictException
import io.limberapp.backend.authorization.principal.JwtRole

internal class UserAlreadyHasRole(role: JwtRole) : ConflictException(
    message = "The user already has the role \"$role\".",
    developerMessage = "This exception should be thrown when an attempt is made to give a user a role but they" +
            " already have that role."
)
