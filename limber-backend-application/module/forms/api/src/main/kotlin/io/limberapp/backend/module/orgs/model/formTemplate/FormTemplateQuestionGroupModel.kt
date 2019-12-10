package io.limberapp.backend.module.orgs.model.formTemplate

import java.util.UUID

data class FormTemplateQuestionGroupModel(
    val id: UUID,
    val questions: List<FormTemplateQuestionModel>
)
