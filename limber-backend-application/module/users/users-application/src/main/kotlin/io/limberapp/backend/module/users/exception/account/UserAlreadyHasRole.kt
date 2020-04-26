package io.limberapp.backend.module.users.exception.account

import com.piperframework.exception.exception.conflict.ConflictException

internal class UserAlreadyHasRole : ConflictException(
    message = "The user already has the role.",
    developerMessage = "This exception should be thrown when an attempt is made to give a user a role but they" +
            " already have that role."
)
