package io.limberapp.backend.module.auth.exception.tenant

import com.piperframework.exception.exception.conflict.ConflictException

internal class Auth0ClientIdAlreadyRegistered(auth0ClientId: String) : ConflictException(
    message = "The Auth0 client ID \"$auth0ClientId\" is already registered to a tenant.",
    developerMessage = "This exception should be thrown when an attempt is made to create a tenant corresponding to" +
            " an Auth0 client ID that is already associated with a tenant."
)
