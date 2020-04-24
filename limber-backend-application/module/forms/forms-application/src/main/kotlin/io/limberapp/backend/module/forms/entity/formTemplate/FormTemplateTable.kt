package io.limberapp.backend.module.forms.entity.formTemplate

import com.piperframework.store.SqlTable

internal object FormTemplateTable : SqlTable("forms", "form_template") {
    val guid = uuid("guid")

    val featureGuid = uuid("feature_guid")

    val title = text("title")

    val description = text("description").nullable()
}
