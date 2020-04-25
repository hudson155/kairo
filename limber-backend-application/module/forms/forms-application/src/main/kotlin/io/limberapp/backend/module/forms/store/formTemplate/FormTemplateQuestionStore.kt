package io.limberapp.backend.module.forms.store.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import java.util.UUID

internal interface FormTemplateQuestionStore : FormTemplateQuestionService {
    fun create(formTemplateGuid: UUID, models: List<FormTemplateQuestionModel>, rank: Int? = null)

    fun get(formTemplateGuid: UUID, questionGuid: UUID): FormTemplateQuestionModel?

    fun getByFormTemplateGuid(formTemplateGuid: UUID): List<FormTemplateQuestionModel>
}
