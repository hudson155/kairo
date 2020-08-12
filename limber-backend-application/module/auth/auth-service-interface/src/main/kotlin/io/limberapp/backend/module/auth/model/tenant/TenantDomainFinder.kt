package io.limberapp.backend.module.auth.model.tenant

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Auth
interface TenantDomainFinder {
  fun orgGuid(orgGuid: UUID)
  fun domain(domain: String)
}
