package io.limberapp.backend.module.orgs.model.org

import java.time.ZonedDateTime
import java.util.*

data class OrgModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val name: String,
    val ownerUserGuid: UUID?,
) {
  data class Update(
      val name: String?,
      val ownerUserGuid: UUID?,
  )
}
