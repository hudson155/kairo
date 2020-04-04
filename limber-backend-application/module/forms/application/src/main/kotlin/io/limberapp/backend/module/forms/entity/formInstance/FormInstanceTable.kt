package io.limberapp.backend.module.forms.entity.formInstance

import com.piperframework.store.SqlTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateTable

internal object FormInstanceTable : SqlTable("forms", "form_instance") {

    val guid = uuid("guid")

    val featureGuid = uuid("feature_guid")

    val formTemplateGuid = uuid("form_template_guid").references(FormTemplateTable.guid)
}
