package io.limberapp.backend.module.forms.store.formInstance

import com.piperframework.finder.SortableFinder
import com.piperframework.store.QueryBuilder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceFinder
import java.util.*

internal class FormInstanceQueryBuilder : QueryBuilder(), FormInstanceFinder {
  override fun featureGuid(featureGuid: UUID) {
    conditions += "feature_guid = :featureGuid"
    bindings["featureGuid"] = featureGuid
  }

  override fun formInstanceGuid(formInstanceGuid: UUID) {
    conditions += "guid = :formInstanceGuid"
    bindings["formInstanceGuid"] = formInstanceGuid
  }

  override fun creatorAccountGuid(creatorAccountGuid: UUID) {
    conditions += "creator_account_guid = :creatorAccountGuid"
    bindings["creatorAccountGuid"] = creatorAccountGuid
  }

  override fun sortBy(sortBy: FormInstanceFinder.SortBy, sortDirection: SortableFinder.SortDirection) {
    val columnName = when (sortBy) {
      FormInstanceFinder.SortBy.NUMBER -> "number"
      FormInstanceFinder.SortBy.SUBMITTED_DATE -> "submitted_date"
    }
    sorts += Pair(columnName, sortDirection == SortableFinder.SortDirection.ASCENDING)
  }
}
