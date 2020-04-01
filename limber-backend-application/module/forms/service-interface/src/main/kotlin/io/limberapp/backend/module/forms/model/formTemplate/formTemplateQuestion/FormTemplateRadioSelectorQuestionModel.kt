package io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDateTime
import java.util.UUID

data class FormTemplateRadioSelectorQuestionModel(
    override val id: UUID,
    override val created: LocalDateTime,
    override val label: String,
    override val helpText: String?,
    val options: Set<String>
) : FormTemplateQuestionModel {
    override val type = FormTemplateQuestionModel.Type.RADIO_SELECTOR

    data class Update(
        override val label: String?,
        override val helpText: String?,
        val options: Set<String>?
    ) : FormTemplateQuestionModel.Update
}