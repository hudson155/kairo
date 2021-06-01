package io.limberapp.exception.tenant

import io.limberapp.exception.ConflictException

class Auth0OrgIdAlreadyRegistered : ConflictException(
    message = "The Auth0 org ID is already registered to a tenant.",
)
