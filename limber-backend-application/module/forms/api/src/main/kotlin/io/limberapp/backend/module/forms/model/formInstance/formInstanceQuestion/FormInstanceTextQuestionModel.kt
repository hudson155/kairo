package io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import java.time.LocalDateTime
import java.util.UUID

data class FormInstanceTextQuestionModel(
    override val created: LocalDateTime,
    override val formTemplateQuestionId: UUID?,
    val text: String
) : FormInstanceQuestionModel {

    override val type = FormInstanceQuestionModel.Type.TEXT
}
