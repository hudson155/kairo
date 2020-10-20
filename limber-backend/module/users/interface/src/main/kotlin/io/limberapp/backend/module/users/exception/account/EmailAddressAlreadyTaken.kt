package io.limberapp.backend.module.users.exception.account

import io.limberapp.common.exception.conflict.ConflictException

class EmailAddressAlreadyTaken : ConflictException(
    message = "The email address is already taken.",
    developerMessage = "This exception should be thrown when an attempt is made to set a user's email address to an" +
        " email address that is already taken by an existing user."
)
