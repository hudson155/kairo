package io.limberapp.backend.module.forms.model.formTemplate

import java.util.UUID

data class FormTemplatePartModel(
    val id: UUID,
    val title: String?,
    val description: String?,
    val questionGroups: List<FormTemplateQuestionGroupModel>
) {

    data class Update(
        val title: String?,
        val description: String?
    )
}
