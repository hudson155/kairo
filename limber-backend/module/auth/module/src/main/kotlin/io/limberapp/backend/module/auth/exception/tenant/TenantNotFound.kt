package io.limberapp.backend.module.auth.exception.tenant

import io.limberapp.exception.notFound.EntityNotFound

internal class TenantNotFound : EntityNotFound("Tenant")
