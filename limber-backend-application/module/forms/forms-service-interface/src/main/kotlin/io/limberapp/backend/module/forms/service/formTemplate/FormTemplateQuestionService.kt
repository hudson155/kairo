package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

interface FormTemplateQuestionService {
    fun create(formTemplateGuid: UUID, model: FormTemplateQuestionModel, rank: Int? = null)

    fun update(
        formTemplateGuid: UUID,
        questionGuid: UUID,
        update: FormTemplateQuestionModel.Update
    ): FormTemplateQuestionModel

    fun delete(formTemplateGuid: UUID, questionGuid: UUID)
}
