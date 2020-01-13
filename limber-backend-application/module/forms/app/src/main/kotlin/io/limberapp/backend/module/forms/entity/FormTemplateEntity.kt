package io.limberapp.backend.module.forms.entity

import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

data class FormTemplateEntity(
    @BsonId val id: UUID,
    val created: LocalDateTime,
    val orgId: UUID,
    val title: String,
    val description: String?,
    val questions: List<FormTemplateQuestionEntity>
) {

    data class Update(
        val title: String?,
        val description: String?
    )

    companion object {
        const val name = "FormTemplate"
    }
}
