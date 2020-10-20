package io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.ZonedDateTime
import java.util.*

data class FormTemplateTextQuestionModel(
    override val guid: UUID,
    override val createdDate: ZonedDateTime,
    override val formTemplateGuid: UUID,
    override val label: String,
    override val helpText: String?,
    override val required: Boolean,
    val multiLine: Boolean,
    val placeholder: String?,
    val validator: Regex?,
) : FormTemplateQuestionModel {
  override val type = FormTemplateQuestionModel.Type.TEXT

  data class Update(
      override val label: String?,
      override val helpText: String?,
      override val required: Boolean?,
      val multiLine: Boolean?,
      val placeholder: String?,
      val validator: Regex?,
  ) : FormTemplateQuestionModel.Update
}
