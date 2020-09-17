package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.common.types.LocalDate
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID

object FormTemplateDateQuestionRep {
  data class Creation(
    override val label: String,
    override val helpText: String? = null,
    override val required: Boolean,
    val earliest: LocalDate? = null,
    val latest: LocalDate? = null,
  ) : FormTemplateQuestionRep.Creation

  data class Complete(
    override val guid: UUID,
    override val createdDate: LocalDateTime,
    override val label: String,
    override val helpText: String?,
    override val required: Boolean,
    val earliest: LocalDate?,
    val latest: LocalDate?,
  ) : FormTemplateQuestionRep.Complete

  data class Update(
    override val label: String? = null,
    override val helpText: String? = null,
    override val required: Boolean? = null,
    val earliest: LocalDate? = null,
    val latest: LocalDate? = null,
  ) : FormTemplateQuestionRep.Update
}
