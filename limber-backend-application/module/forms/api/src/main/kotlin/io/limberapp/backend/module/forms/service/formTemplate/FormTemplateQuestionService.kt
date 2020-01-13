package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

interface FormTemplateQuestionService {

    fun create(formTemplateId: UUID, model: FormTemplateQuestionModel, index: Int? = null)

    fun get(formTemplateId: UUID, formTemplateQuestionId: UUID): FormTemplateQuestionModel?

    fun update(
        formTemplateId: UUID,
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionModel.Update
    ): FormTemplateQuestionModel

    fun delete(formTemplateId: UUID, formTemplateQuestionId: UUID)
}
