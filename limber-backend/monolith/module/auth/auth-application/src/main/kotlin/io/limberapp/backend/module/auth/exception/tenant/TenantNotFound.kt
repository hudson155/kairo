package io.limberapp.backend.module.auth.exception.tenant

import io.limberapp.common.exception.exception.notFound.EntityNotFound

internal class TenantNotFound : EntityNotFound("Tenant")
