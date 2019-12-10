package io.limberapp.backend.module.orgs.model.formTemplate

import java.util.UUID

interface FormTemplateQuestionModel {

    val id: UUID
    val label: String
    val helpText: String?
    val width: Width

    enum class Width { HALF_WIDTH, FULL_WIDTH }

    interface Update {
        val label: String?
        val helpText: String?
        val width: Width?
    }
}
