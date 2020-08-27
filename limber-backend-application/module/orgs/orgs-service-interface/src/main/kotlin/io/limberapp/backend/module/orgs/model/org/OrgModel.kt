package io.limberapp.backend.module.orgs.model.org

import java.time.LocalDateTime
import java.util.*

data class OrgModel(
  val guid: UUID,
  val createdDate: LocalDateTime,
  val name: String,
  val ownerAccountGuid: UUID?,
) {
  data class Update(
    val name: String?,
    val ownerAccountGuid: UUID?,
  )
}
