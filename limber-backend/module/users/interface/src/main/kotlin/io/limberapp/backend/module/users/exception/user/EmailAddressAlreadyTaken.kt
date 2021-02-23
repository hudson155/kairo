package io.limberapp.backend.module.users.exception.user

import io.limberapp.common.exception.conflict.ConflictException

class EmailAddressAlreadyTaken : ConflictException(
    message = "The email address is already taken.",
)
