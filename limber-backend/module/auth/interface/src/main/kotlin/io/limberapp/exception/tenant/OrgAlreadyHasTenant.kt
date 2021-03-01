package io.limberapp.exception.tenant

import io.limberapp.exception.ConflictException

class OrgAlreadyHasTenant : ConflictException(
    message = "This org already has a tenant.",
)
