package io.limberapp.backend.module.users.exception.account

import io.limberapp.exception.conflict.ConflictException

class UserDoesNotHaveRole : ConflictException(
    message = "The user does not have the role.",
    developerMessage = "This exception should be thrown when an attempt is made to remove a user's role but they do" +
        " not have that role."
)