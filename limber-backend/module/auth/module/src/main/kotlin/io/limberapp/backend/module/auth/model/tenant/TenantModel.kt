package io.limberapp.backend.module.auth.model.tenant

import java.time.ZonedDateTime
import java.util.*

data class TenantModel(
    val createdDate: ZonedDateTime,
    val orgGuid: UUID,
    val name: String,
    val auth0ClientId: String,
) {
  data class Update(
      val name: String?,
      val auth0ClientId: String?,
  )
}
