package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.util.*

@LimberModule.Forms
interface FormTemplateQuestionService {
  fun createDefaults(featureGuid: UUID, formTemplateGuid: UUID): List<FormTemplateQuestionModel>

  fun create(featureGuid: UUID, model: FormTemplateQuestionModel, rank: Int? = null): FormTemplateQuestionModel

  fun getByFormTemplateGuid(featureGuid: UUID, formTemplateGuid: UUID): List<FormTemplateQuestionModel>

  fun update(
    featureGuid: UUID,
    formTemplateGuid: UUID,
    questionGuid: UUID,
    update: FormTemplateQuestionModel.Update,
  ): FormTemplateQuestionModel

  fun delete(featureGuid: UUID, formTemplateGuid: UUID, questionGuid: UUID)
}
