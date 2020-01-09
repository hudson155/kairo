package io.limberapp.backend.module.forms.model.formTemplate

import java.util.UUID

data class FormTemplateQuestionGroupModel(
    val id: UUID,
    val questions: List<FormTemplateQuestionModel>
)
