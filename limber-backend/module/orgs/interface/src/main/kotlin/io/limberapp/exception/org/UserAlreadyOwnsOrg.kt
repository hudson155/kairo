package io.limberapp.exception.org

import io.limberapp.exception.ConflictException

class UserAlreadyOwnsOrg : ConflictException(
    message = "The user already owns an org. Users cannot own multiple orgs.",
)
