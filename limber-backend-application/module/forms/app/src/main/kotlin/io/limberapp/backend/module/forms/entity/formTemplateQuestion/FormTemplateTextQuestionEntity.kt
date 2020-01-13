package io.limberapp.backend.module.forms.entity.formTemplateQuestion

import io.limberapp.backend.module.forms.entity.FormTemplateQuestionEntity
import java.time.LocalDateTime
import java.util.UUID

data class FormTemplateTextQuestionEntity(
    override val id: UUID,
    override val created: LocalDateTime,
    override val label: String,
    override val helpText: String?,
    val multiLine: Boolean,
    val placeholder: String?,
    val validator: Regex?
) : FormTemplateQuestionEntity {

    data class Update(
        override val label: String?,
        override val helpText: String?,
        val multiLine: Boolean?,
        val placeholder: String?,
        val validator: Regex?
    ) : FormTemplateQuestionEntity.Update
}
