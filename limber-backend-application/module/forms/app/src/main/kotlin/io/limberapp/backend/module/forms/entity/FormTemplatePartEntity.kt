package io.limberapp.backend.module.forms.entity

import com.piperframework.entity.CompleteSubentity
import com.piperframework.entity.UpdateSubentity
import java.util.UUID

data class FormTemplatePartEntity(
    val id: UUID,
    val title: String?,
    val description: String?,
    val questionGroups: List<FormTemplateQuestionGroupEntity>
) : CompleteSubentity {

    data class Update(
        val title: String?,
        val description: String?
    ) : UpdateSubentity
}
