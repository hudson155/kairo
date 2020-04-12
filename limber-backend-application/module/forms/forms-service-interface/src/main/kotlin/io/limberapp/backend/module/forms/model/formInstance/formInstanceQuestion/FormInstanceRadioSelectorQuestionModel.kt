package io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDateTime
import java.util.UUID

data class FormInstanceRadioSelectorQuestionModel(
    override val created: LocalDateTime,
    override val formTemplateQuestionId: UUID?,
    val selections: Set<String>
) : FormInstanceQuestionModel {

    override val type = FormTemplateQuestionModel.Type.RADIO_SELECTOR

    data class Update(
        val selections: Set<String>?
    ) : FormInstanceQuestionModel.Update
}
