package io.limberapp.exception.user

import io.limberapp.exception.ConflictException

class EmailAddressAlreadyTaken : ConflictException(
    message = "The email address is already taken.",
)
