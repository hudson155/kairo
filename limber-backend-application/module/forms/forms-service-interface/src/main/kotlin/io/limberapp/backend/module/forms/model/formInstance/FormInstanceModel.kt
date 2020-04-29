package io.limberapp.backend.module.forms.model.formInstance

import java.time.LocalDateTime
import java.util.UUID

data class FormInstanceModel(
    val guid: UUID,
    val createdDate: LocalDateTime,
    val featureGuid: UUID,
    val formTemplateGuid: UUID
)
