package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.util.*

interface FormTemplateQuestionService {
  fun createDefaults(formTemplateGuid: UUID): List<FormTemplateQuestionModel>

  fun create(model: FormTemplateQuestionModel, rank: Int? = null)

  fun getByFormTemplateGuid(formTemplateGuid: UUID): List<FormTemplateQuestionModel>

  fun update(
    formTemplateGuid: UUID,
    questionGuid: UUID,
    update: FormTemplateQuestionModel.Update
  ): FormTemplateQuestionModel

  fun delete(formTemplateGuid: UUID, questionGuid: UUID)
}
