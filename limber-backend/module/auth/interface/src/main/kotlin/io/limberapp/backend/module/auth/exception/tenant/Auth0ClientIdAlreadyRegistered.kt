package io.limberapp.backend.module.auth.exception.tenant

import io.limberapp.exception.conflict.ConflictException

class Auth0ClientIdAlreadyRegistered : ConflictException(
  message = "The Auth0 client ID is already registered to a tenant.",
  developerMessage = "This exception should be thrown when an attempt is made to create a tenant corresponding to" +
    " an Auth0 client ID that is already associated with a tenant."
)
