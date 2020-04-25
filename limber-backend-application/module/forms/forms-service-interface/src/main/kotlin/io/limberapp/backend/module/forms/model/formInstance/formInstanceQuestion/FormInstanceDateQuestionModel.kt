package io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class FormInstanceDateQuestionModel(
    override val createdDate: LocalDateTime,
    override val questionGuid: UUID?,
    val date: LocalDate
) : FormInstanceQuestionModel {
    override val type = FormTemplateQuestionModel.Type.DATE

    data class Update(
        val date: LocalDate?
    ) : FormInstanceQuestionModel.Update
}
