package io.limberapp.backend.module.forms.entity.formTemplateQuestion

import io.limberapp.backend.module.forms.entity.FormTemplateQuestionEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class FormTemplateDateQuestionEntity(
    override val id: UUID,
    override val created: LocalDateTime,
    override val label: String,
    override val helpText: String?,
    val earliest: LocalDate?,
    val latest: LocalDate?
) : FormTemplateQuestionEntity {

    data class Update(
        override val label: String?,
        override val helpText: String?,
        val earliest: LocalDate?,
        val latest: LocalDate?
    ) : FormTemplateQuestionEntity.Update
}
