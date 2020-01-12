package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionGroupModel
import java.util.UUID

interface FormTemplateQuestionGroupService {

    fun create(formTemplateId: UUID, formTemplatePartId: UUID, model: FormTemplateQuestionGroupModel)

    fun get(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID
    ): FormTemplateQuestionGroupModel?

    fun delete(formTemplateId: UUID, formTemplatePartId: UUID, formTemplateQuestionGroupId: UUID)
}
