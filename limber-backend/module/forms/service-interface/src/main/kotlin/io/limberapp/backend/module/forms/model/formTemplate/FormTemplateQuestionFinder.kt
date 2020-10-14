package io.limberapp.backend.module.forms.model.formTemplate

import java.util.*

interface FormTemplateQuestionFinder {
  fun featureGuid(featureGuid: UUID)
  fun formTemplateGuid(formTemplateGuid: UUID)
  fun questionGuid(questionGuid: UUID)
}
