package io.limberapp.model.feature

import java.time.ZonedDateTime
import java.util.UUID

data class FeatureModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val orgGuid: UUID,
    val name: String,
    val path: String,
    val type: Type,
    val rank: Int,
    val isDefaultFeature: Boolean,
) {
  enum class Type {
    FORMS,
    HOME;
  }

  data class Update(
      val name: String?,
      val path: String?,
      val rank: Int?,
      val isDefaultFeature: Boolean?,
  )
}
