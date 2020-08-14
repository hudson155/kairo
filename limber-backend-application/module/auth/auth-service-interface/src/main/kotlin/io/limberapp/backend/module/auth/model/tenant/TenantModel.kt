package io.limberapp.backend.module.auth.model.tenant

import java.time.LocalDateTime
import java.util.*

data class TenantModel(
  val createdDate: LocalDateTime,
  val orgGuid: UUID,
  val auth0ClientId: String,
) {
  data class Update(
    val auth0ClientId: String?,
  )
}
