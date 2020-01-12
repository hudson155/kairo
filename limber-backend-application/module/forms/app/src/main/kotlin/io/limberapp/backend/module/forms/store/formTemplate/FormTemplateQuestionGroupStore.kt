package io.limberapp.backend.module.forms.store.formTemplate

import com.piperframework.store.Store
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionGroupModel
import java.util.UUID

internal interface FormTemplateQuestionGroupStore : Store {

    fun create(formTemplateId: UUID, formTemplatePartId: UUID, model: FormTemplateQuestionGroupModel)

    fun get(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID
    ): FormTemplateQuestionGroupModel?

    fun delete(formTemplateId: UUID, formTemplatePartId: UUID, formTemplateQuestionGroupId: UUID)
}
