package io.limberapp.backend.module.auth.exception.tenant

import io.limberapp.exception.conflict.ConflictException

internal class TenantDomainAlreadyRegistered : ConflictException(
  message = "The tenant domain is already registered to a tenant.",
  developerMessage = "This exception should be thrown when an attempt is made to create a tenant corresponding to" +
    " a domain that is already associated with a tenant."
)
