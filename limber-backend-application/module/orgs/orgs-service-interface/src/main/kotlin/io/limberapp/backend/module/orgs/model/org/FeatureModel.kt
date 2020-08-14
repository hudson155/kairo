package io.limberapp.backend.module.orgs.model.org

import java.time.LocalDateTime
import java.util.*

data class FeatureModel(
  val guid: UUID,
  val createdDate: LocalDateTime,
  val orgGuid: UUID,
  val rank: Int,
  val name: String,
  val path: String,
  val type: Type,
  val isDefaultFeature: Boolean,
) {
  enum class Type {
    FORMS,
    HOME;
  }

  data class Update(
    val rank: Int?,
    val name: String?,
    val path: String?,
    val isDefaultFeature: Boolean?,
  )
}
