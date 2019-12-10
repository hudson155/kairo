package io.limberapp.backend.module.forms.entity.formTemplateQuestion

import io.limberapp.backend.module.forms.entity.FormTemplateQuestionEntity
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDate
import java.util.UUID

data class FormTemplateDateQuestionEntity(
    override val id: UUID,
    override val label: String,
    override val helpText: String?,
    override val width: FormTemplateQuestionModel.Width,
    val earliest: LocalDate?,
    val latest: LocalDate?
) : FormTemplateQuestionEntity {

    data class Update(
        override val label: String?,
        override val helpText: String?,
        override val width: FormTemplateQuestionModel.Width?,
        val earliest: LocalDate?,
        val latest: LocalDate?
    ) : FormTemplateQuestionEntity.Update
}
