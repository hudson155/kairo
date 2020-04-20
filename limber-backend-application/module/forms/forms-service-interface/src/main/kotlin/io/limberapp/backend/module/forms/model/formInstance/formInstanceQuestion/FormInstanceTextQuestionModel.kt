package io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDateTime
import java.util.UUID

data class FormInstanceTextQuestionModel(
    override val created: LocalDateTime,
    override val questionId: UUID?,
    val text: String
) : FormInstanceQuestionModel {

    override val type = FormTemplateQuestionModel.Type.TEXT

    data class Update(
        val text: String?
    ) : FormInstanceQuestionModel.Update
}
