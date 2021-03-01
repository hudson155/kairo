package io.limberapp.exception.tenant

import io.limberapp.exception.ConflictException

class Auth0ClientIdAlreadyRegistered : ConflictException(
    message = "The Auth0 client ID is already registered to a tenant.",
)
