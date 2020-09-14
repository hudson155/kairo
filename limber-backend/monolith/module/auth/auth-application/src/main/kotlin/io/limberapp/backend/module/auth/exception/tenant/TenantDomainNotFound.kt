package io.limberapp.backend.module.auth.exception.tenant

import com.piperframework.exception.exception.notFound.EntityNotFound

internal class TenantDomainNotFound : EntityNotFound("Tenant Domain")
