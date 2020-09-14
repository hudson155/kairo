package io.limberapp.backend.module.forms.model.formInstance

import com.piperframework.finder.SortableFinder
import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Forms
interface FormInstanceFinder : SortableFinder<FormInstanceFinder.SortBy> {
  fun featureGuid(featureGuid: UUID)
  fun formInstanceGuid(formInstanceGuid: UUID)
  fun creatorAccountGuid(creatorAccountGuid: UUID)

  enum class SortBy { NUMBER, SUBMITTED_DATE }
}
