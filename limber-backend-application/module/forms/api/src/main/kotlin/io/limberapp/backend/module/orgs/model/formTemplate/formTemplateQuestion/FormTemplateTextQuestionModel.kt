package io.limberapp.backend.module.orgs.model.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

data class FormTemplateTextQuestionModel(
    override val id: UUID,
    override val label: String,
    override val helpText: String,
    override val width: FormTemplateQuestionModel.Width,
    val multiLine: Boolean,
    val placeholder: String?,
    val validator: Regex?
) : FormTemplateQuestionModel
