package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

interface FormTemplateQuestionService {

    fun create(formTemplateId: UUID, model: FormTemplateQuestionModel, rank: Int? = null)

    fun update(
        formTemplateId: UUID,
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionModel.Update
    ): FormTemplateQuestionModel

    fun delete(formTemplateId: UUID, formTemplateQuestionId: UUID)
}
