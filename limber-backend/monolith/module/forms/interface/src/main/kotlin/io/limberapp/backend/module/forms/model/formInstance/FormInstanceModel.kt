package io.limberapp.backend.module.forms.model.formInstance

import java.time.LocalDateTime
import java.util.*

data class FormInstanceModel(
  val guid: UUID,
  val createdDate: LocalDateTime,
  val featureGuid: UUID,
  val formTemplateGuid: UUID,
  val number: Long?,
  val submittedDate: LocalDateTime?,
  val creatorAccountGuid: UUID,
) {
  data class Update(
    val setNumber: Boolean,
    val submittedDate: LocalDateTime?,
  )
}
