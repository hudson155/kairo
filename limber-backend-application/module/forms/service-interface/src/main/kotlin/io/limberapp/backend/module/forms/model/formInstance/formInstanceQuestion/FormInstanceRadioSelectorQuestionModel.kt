package io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDateTime
import java.util.UUID

class FormInstanceRadioSelectorQuestionModel(
    override val created: LocalDateTime,
    override val formTemplateQuestionId: UUID?,
    val selection: Set<String>
): FormInstanceQuestionModel {
    override val type = FormTemplateQuestionModel.Type.RADIO_SELECTOR

    data class Update(
        val selection: Set<String>?
    ) : FormInstanceQuestionModel.Update
}