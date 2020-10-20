package io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

data class FormInstanceDateQuestionModel(
    override val createdDate: ZonedDateTime,
    override val formInstanceGuid: UUID,
    override val questionGuid: UUID?,
    val date: LocalDate,
) : FormInstanceQuestionModel {
  override val type = FormTemplateQuestionModel.Type.DATE
}
