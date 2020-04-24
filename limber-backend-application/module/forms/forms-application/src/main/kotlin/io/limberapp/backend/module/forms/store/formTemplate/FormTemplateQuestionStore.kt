package io.limberapp.backend.module.forms.store.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import java.util.UUID

internal interface FormTemplateQuestionStore : FormTemplateQuestionService {
    fun create(formTemplateId: UUID, models: List<FormTemplateQuestionModel>, rank: Int? = null)

    fun get(formTemplateId: UUID, questionId: UUID): FormTemplateQuestionModel?

    fun getByFormTemplateId(formTemplateId: UUID): List<FormTemplateQuestionModel>
}
