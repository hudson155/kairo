package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionFinder
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.common.finder.Finder
import java.util.*

interface FormTemplateQuestionService : Finder<FormTemplateQuestionModel, FormTemplateQuestionFinder> {
  fun create(featureGuid: UUID, model: FormTemplateQuestionModel, rank: Int? = null): FormTemplateQuestionModel

  fun update(
      featureGuid: UUID,
      formTemplateGuid: UUID,
      questionGuid: UUID,
      update: FormTemplateQuestionModel.Update,
  ): FormTemplateQuestionModel

  fun delete(featureGuid: UUID, formTemplateGuid: UUID, questionGuid: UUID)
}
