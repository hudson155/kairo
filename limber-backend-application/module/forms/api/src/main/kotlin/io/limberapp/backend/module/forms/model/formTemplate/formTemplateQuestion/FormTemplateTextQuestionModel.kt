package io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDateTime
import java.util.UUID

data class FormTemplateTextQuestionModel(
    override val id: UUID,
    override val created: LocalDateTime,
    override val label: String,
    override val helpText: String?,
    val multiLine: Boolean,
    val placeholder: String?,
    val validator: Regex?
) : FormTemplateQuestionModel {

    data class Update(
        override val label: String?,
        override val helpText: String?,
        val multiLine: Boolean?,
        val placeholder: String?,
        val validator: Regex?
    ) : FormTemplateQuestionModel.Update
}
