package io.limberapp.model.org

import java.time.ZonedDateTime
import java.util.UUID

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
