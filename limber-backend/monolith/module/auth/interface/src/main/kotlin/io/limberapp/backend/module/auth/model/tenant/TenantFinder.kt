package io.limberapp.backend.module.auth.model.tenant

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Auth
interface TenantFinder {
  fun orgGuid(orgGuid: UUID)
  fun auth0ClientId(auth0ClientId: String)
  fun domain(domain: String)
}
