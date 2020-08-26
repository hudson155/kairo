package io.limberapp.backend.module.forms.store.formInstance

import com.piperframework.store.QueryBuilder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionFinder
import java.util.*

internal class FormInstanceQuestionQueryBuilder : QueryBuilder(), FormInstanceQuestionFinder {
  override fun featureGuid(featureGuid: UUID) {
    conditions += "(SELECT feature_guid FROM forms.form_instance WHERE guid = form_instance_guid) = :featureGuid"
    bindings["featureGuid"] = featureGuid
  }

  override fun formInstanceGuid(formInstanceGuid: UUID) {
    conditions += "form_instance_guid = :formInstanceGuid"
    bindings["formInstanceGuid"] = formInstanceGuid
  }

  override fun questionGuid(questionGuid: UUID) {
    conditions += "question_guid = :questionGuid"
    bindings["questionGuid"] = questionGuid
  }
}
