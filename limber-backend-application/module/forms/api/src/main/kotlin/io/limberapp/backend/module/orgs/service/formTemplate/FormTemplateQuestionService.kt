package io.limberapp.backend.module.orgs.service.formTemplate

import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

interface FormTemplateQuestionService {

    fun create(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        model: FormTemplateQuestionModel,
        index: Short? = null
    )

    fun update(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        id: UUID,
        update: FormTemplateQuestionModel.Update
    ): FormTemplateQuestionModel

    fun delete(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        id: UUID
    )
}
