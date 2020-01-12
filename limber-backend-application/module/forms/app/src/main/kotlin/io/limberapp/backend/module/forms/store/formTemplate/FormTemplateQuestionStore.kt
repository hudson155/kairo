package io.limberapp.backend.module.forms.store.formTemplate

import com.piperframework.store.Store
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionEntity
import java.util.UUID

internal interface FormTemplateQuestionStore : Store {

    fun create(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        entity: FormTemplateQuestionEntity
    )

    fun get(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID
    ): FormTemplateQuestionEntity?

    fun update(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionEntity.Update
    ): FormTemplateQuestionEntity

    fun delete(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID
    )
}
