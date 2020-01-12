package io.limberapp.backend.module.forms.entity

import java.util.UUID

data class FormTemplatePartEntity(
    val id: UUID,
    val title: String?,
    val description: String?,
    val questionGroups: List<FormTemplateQuestionGroupEntity>
) {

    data class Update(
        val title: String?,
        val description: String?
    )
}
