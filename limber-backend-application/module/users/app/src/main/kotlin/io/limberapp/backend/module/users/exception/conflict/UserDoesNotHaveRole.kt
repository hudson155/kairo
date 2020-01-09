package io.limberapp.backend.module.users.exception.conflict

import com.piperframework.exception.exception.conflict.ConflictException
import io.limberapp.backend.authorization.principal.JwtRole

internal class UserDoesNotHaveRole(role: JwtRole) : ConflictException(
    message = "The user does not have the role \"$role\".",
    developerMessage = "This exception should be thrown when an attempt is made to remove a user's role but they do" +
            " not have that role."
)
