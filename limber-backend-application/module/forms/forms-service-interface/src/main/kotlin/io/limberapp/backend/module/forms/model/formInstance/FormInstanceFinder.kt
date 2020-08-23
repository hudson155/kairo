package io.limberapp.backend.module.forms.model.formInstance

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Forms
interface FormInstanceFinder {
  fun featureGuid(featureGuid: UUID)
  fun formInstanceGuid(formInstanceGuid: UUID)
  fun creatorAccountGuid(creatorAccountGuid: UUID)
}
