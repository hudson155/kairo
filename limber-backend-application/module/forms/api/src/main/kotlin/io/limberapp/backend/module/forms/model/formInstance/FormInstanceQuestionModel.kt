package io.limberapp.backend.module.forms.model.formInstance

import java.time.LocalDateTime
import java.util.UUID

interface FormInstanceQuestionModel {

    val created: LocalDateTime
    val formTemplateQuestionId: UUID?
    val type: Type

    enum class Type {
        DATE,
        TEXT;
    }
}
