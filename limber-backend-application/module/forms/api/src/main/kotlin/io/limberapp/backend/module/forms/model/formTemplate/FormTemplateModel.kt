package io.limberapp.backend.module.forms.model.formTemplate

import java.time.LocalDateTime
import java.util.UUID

data class FormTemplateModel(
    val id: UUID,
    val created: LocalDateTime,
    val orgId: UUID,
    val title: String,
    val description: String?,
    val parts: List<FormTemplatePartModel>
) {

    data class Update(
        val title: String?,
        val description: String?
    )
}
