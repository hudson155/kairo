package io.limberapp.backend.module.forms.entity

import java.util.UUID

data class FormTemplateQuestionGroupEntity(
    val id: UUID,
    val questions: List<FormTemplateQuestionEntity>
)
