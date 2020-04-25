package io.limberapp.backend.module.forms.model.formTemplate

import java.time.LocalDateTime
import java.util.UUID

data class FormTemplateModel(
    val guid: UUID,
    val createdDate: LocalDateTime,
    val featureGuid: UUID,
    val title: String,
    val description: String?,
    val questions: List<FormTemplateQuestionModel>
) {
    data class Update(
        val title: String?,
        val description: String?
    )
}
