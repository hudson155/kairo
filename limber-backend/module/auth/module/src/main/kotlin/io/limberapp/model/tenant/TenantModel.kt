package io.limberapp.model.tenant

import java.time.ZonedDateTime
import java.util.UUID

data class TenantModel(
    val createdDate: ZonedDateTime,
    val orgGuid: UUID,
    val auth0OrgId: String,
) {
  data class Update(
      val auth0OrgId: String?,
  )
}
