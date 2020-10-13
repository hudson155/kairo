package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import java.time.LocalDateTime
import java.util.*

object FormTemplateYesNoQuestionRep {
  data class Creation(
      override val label: String,
      override val helpText: String? = null,
      override val required: Boolean,
  ) : FormTemplateQuestionRep.Creation

  data class Complete(
      override val guid: UUID,
      override val createdDate: LocalDateTime,
      override val label: String,
      override val helpText: String?,
      override val required: Boolean,
  ) : FormTemplateQuestionRep.Complete

  data class Update(
      override val label: String? = null,
      override val helpText: String? = null,
      override val required: Boolean? = null,
  ) : FormTemplateQuestionRep.Update
}
