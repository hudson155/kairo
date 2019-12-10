package io.limberapp.backend.module.forms.entity

import com.piperframework.entity.CompleteEntity
import com.piperframework.entity.UpdateEntity
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.UUID

data class FormTemplateEntity(
    @BsonId override val id: UUID,
    override val created: LocalDateTime,
    val orgId: UUID,
    val title: String,
    val description: String?,
    val parts: List<FormTemplatePartEntity>
) : CompleteEntity {

    data class Update(
        val title: String?,
        val description: String?
    ) : UpdateEntity

    companion object {
        const val collectionName = "FormTemplate"
    }
}
