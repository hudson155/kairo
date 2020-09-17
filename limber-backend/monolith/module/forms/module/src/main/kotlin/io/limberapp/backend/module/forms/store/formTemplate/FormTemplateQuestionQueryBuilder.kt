package io.limberapp.backend.module.forms.store.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionFinder
import io.limberapp.common.store.QueryBuilder
import java.util.*

internal class FormTemplateQuestionQueryBuilder : QueryBuilder(), FormTemplateQuestionFinder {
  override fun featureGuid(featureGuid: UUID) {
    conditions += "(SELECT feature_guid FROM forms.form_template WHERE guid = form_template_guid) = :featureGuid"
    bindings["featureGuid"] = featureGuid
  }

  override fun formTemplateGuid(formTemplateGuid: UUID) {
    conditions += "form_template_guid = :formTemplateGuid"
    bindings["formTemplateGuid"] = formTemplateGuid
  }

  override fun questionGuid(questionGuid: UUID) {
    conditions += "guid = :questionGuid"
    bindings["questionGuid"] = questionGuid
  }
}
