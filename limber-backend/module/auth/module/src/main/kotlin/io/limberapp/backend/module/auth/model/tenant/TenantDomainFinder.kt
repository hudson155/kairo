package io.limberapp.backend.module.auth.model.tenant

import java.util.*

interface TenantDomainFinder {
  fun orgGuid(orgGuid: UUID)
  fun domain(domain: String)
}
