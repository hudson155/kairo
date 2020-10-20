package io.limberapp.backend.module.forms.model.formInstance

import java.time.ZonedDateTime
import java.util.*

data class FormInstanceModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val featureGuid: UUID,
    val formTemplateGuid: UUID,
    val number: Long?,
    val submittedDate: ZonedDateTime?,
    val creatorAccountGuid: UUID,
) {
  val isSubmitted = submittedDate != null
  val isDraft = submittedDate == null

  data class Update(
      val setNumber: Boolean,
      val submittedDate: ZonedDateTime?,
  )
}
