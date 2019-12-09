package io.limberapp.backend.module.orgs.model.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDate
import java.util.UUID

data class FormTemplateDateQuestionModel(
    override val id: UUID,
    override val label: String,
    override val helpText: String,
    override val width: FormTemplateQuestionModel.Width,
    val earliest: LocalDate?,
    val latest: LocalDate?
) : FormTemplateQuestionModel
