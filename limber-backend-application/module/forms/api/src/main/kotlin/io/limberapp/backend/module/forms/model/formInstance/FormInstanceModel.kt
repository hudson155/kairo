package io.limberapp.backend.module.forms.model.formInstance

import java.time.LocalDateTime
import java.util.UUID

data class FormInstanceModel(
    val id: UUID,
    val created: LocalDateTime,
    val featureId: UUID,
    val formTemplateId: UUID,
    val questions: List<FormInstanceQuestionModel>
)
