package io.limberapp.backend.module.auth.exception.tenant

import com.piperframework.exception.exception.conflict.ConflictException

internal class TenantDomainAlreadyRegistered(tenantDomain: String?) : ConflictException(
    message = "The tenant domain${tenantDomain?.let { " \"$it\"" }.orEmpty()} is already registered to a tenant.",
    developerMessage = "This exception should be thrown when an attempt is made to create a tenant corresponding to" +
            " a domain that is already associated with a tenant."
)
