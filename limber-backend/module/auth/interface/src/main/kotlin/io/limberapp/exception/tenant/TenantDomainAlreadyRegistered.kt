package io.limberapp.exception.tenant

import io.limberapp.exception.ConflictException

class TenantDomainAlreadyRegistered : ConflictException(
    message = "The tenant domain is already registered to a tenant.",
)
