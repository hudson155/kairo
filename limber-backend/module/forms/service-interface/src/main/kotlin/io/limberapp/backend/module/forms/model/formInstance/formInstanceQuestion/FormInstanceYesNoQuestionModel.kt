package io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDateTime
import java.util.*

data class FormInstanceYesNoQuestionModel(
  override val createdDate: LocalDateTime,
  override val formInstanceGuid: UUID,
  override val questionGuid: UUID?,
  val yes: Boolean,
) : FormInstanceQuestionModel {
  override val type = FormTemplateQuestionModel.Type.YES_NO
}
