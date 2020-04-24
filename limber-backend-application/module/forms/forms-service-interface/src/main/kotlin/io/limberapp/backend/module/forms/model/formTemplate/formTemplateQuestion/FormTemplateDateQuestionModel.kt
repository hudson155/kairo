package io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class FormTemplateDateQuestionModel(
    override val id: UUID,
    override val created: LocalDateTime,
    override val label: String,
    override val helpText: String?,
    val earliest: LocalDate?,
    val latest: LocalDate?
) : FormTemplateQuestionModel {
    override val type = FormTemplateQuestionModel.Type.DATE

    data class Update(
        override val label: String?,
        override val helpText: String?,
        val earliest: LocalDate?,
        val latest: LocalDate?
    ) : FormTemplateQuestionModel.Update
}
