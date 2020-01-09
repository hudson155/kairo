package io.limberapp.backend.module.forms.store.formTemplate

import com.piperframework.store.Store
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionGroupEntity
import java.util.UUID

internal interface FormTemplateQuestionGroupStore : Store<FormTemplateEntity> {

    fun create(formTemplateId: UUID, formTemplatePartId: UUID, entity: FormTemplateQuestionGroupEntity)

    fun get(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID
    ): FormTemplateQuestionGroupEntity?

    fun delete(formTemplateId: UUID, formTemplatePartId: UUID, formTemplateQuestionGroupId: UUID)
}
