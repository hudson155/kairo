package io.limberapp.backend.module.auth.exception.tenant

import com.piperframework.exception.exception.conflict.ConflictException
import java.util.UUID

internal class OrgAlreadyHasTenant(orgId: UUID) : ConflictException(
    message = "The org with ID \"$orgId\" already has a tenant.",
    developerMessage = "This exception should be thrown when an attempt is made to create a tenant corresponding to" +
            " an org that is already associated with a tenant."
)
