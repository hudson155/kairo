package io.limberapp.backend.module.forms.model.formTemplate

import java.time.ZonedDateTime
import java.util.*

data class FormTemplateModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val featureGuid: UUID,
    val title: String,
    val description: String?,
) {
  data class Update(
      val title: String?,
      val description: String?,
  )
}
