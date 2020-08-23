package io.limberapp.backend.module.forms.model.formTemplate

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Forms
interface FormTemplateFinder {
  fun featureGuid(featureGuid: UUID)
  fun formTemplateGuid(formTemplateGuid: UUID)
}
