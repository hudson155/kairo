package io.limberapp.backend.module.forms.entity.formTemplateQuestion

import io.limberapp.backend.module.forms.entity.FormTemplateQuestionEntity
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

data class FormTemplateTextQuestionEntity(
    override val id: UUID,
    override val label: String,
    override val helpText: String?,
    override val width: FormTemplateQuestionModel.Width,
    val multiLine: Boolean,
    val placeholder: String?,
    val validator: Regex?
) : FormTemplateQuestionEntity {

    data class Update(
        override val label: String?,
        override val helpText: String?,
        override val width: FormTemplateQuestionModel.Width?,
        val multiLine: Boolean?,
        val placeholder: String?,
        val validator: Regex?
    ) : FormTemplateQuestionEntity.Update
}
