package io.limberapp.backend.module.forms.model.formInstance

import io.limberapp.common.finder.SortableFinder
import java.util.*

interface FormInstanceFinder : SortableFinder<FormInstanceFinder.SortBy> {
  fun featureGuid(featureGuid: UUID)
  fun formInstanceGuid(formInstanceGuid: UUID)
  fun creatorAccountGuid(creatorAccountGuid: UUID)

  enum class SortBy { NUMBER, SUBMITTED_DATE }
}
