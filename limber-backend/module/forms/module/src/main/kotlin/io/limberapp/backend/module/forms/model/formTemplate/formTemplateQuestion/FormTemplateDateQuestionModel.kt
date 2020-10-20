package io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

data class FormTemplateDateQuestionModel(
    override val guid: UUID,
    override val createdDate: ZonedDateTime,
    override val formTemplateGuid: UUID,
    override val label: String,
    override val helpText: String?,
    override val required: Boolean,
    val earliest: LocalDate?,
    val latest: LocalDate?,
) : FormTemplateQuestionModel {
  override val type = FormTemplateQuestionModel.Type.DATE

  data class Update(
      override val label: String?,
      override val helpText: String?,
      override val required: Boolean?,
      val earliest: LocalDate?,
      val latest: LocalDate?,
  ) : FormTemplateQuestionModel.Update
}
