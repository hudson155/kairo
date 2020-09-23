package io.limberapp.backend.module.forms.store.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateFinder
import io.limberapp.common.store.QueryBuilder
import java.util.*

internal class FormTemplateQueryBuilder : QueryBuilder(), FormTemplateFinder {
  override fun featureGuid(featureGuid: UUID) {
    conditions += "feature_guid = :featureGuid"
    bindings["featureGuid"] = featureGuid
  }

  override fun formTemplateGuid(formTemplateGuid: UUID) {
    conditions += "guid = :formTemplateGuid"
    bindings["formTemplateGuid"] = formTemplateGuid
  }
}
