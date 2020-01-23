package io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class FormInstanceDateQuestionModel(
    override val created: LocalDateTime,
    override val formTemplateQuestionId: UUID?,
    val date: LocalDate
) : FormInstanceQuestionModel {

    override val type = FormInstanceQuestionModel.Type.DATE
}
