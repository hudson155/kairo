package io.limberapp.backend.module.forms.model.formTemplate

import java.util.*

interface FormTemplateFinder {
  fun featureGuid(featureGuid: UUID)
  fun formTemplateGuid(formTemplateGuid: UUID)
}
