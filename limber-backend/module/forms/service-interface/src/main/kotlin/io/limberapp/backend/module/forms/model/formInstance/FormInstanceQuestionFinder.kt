package io.limberapp.backend.module.forms.model.formInstance

import java.util.*

interface FormInstanceQuestionFinder {
  fun featureGuid(featureGuid: UUID)
  fun formInstanceGuid(formInstanceGuid: UUID)
  fun questionGuid(questionGuid: UUID)
}
