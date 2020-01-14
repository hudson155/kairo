package io.limberapp.backend.module.forms.entity

import com.piperframework.store.SqlTable

object FormTemplateTable : SqlTable("forms", "form_template") {
    val guid = uuid("guid")
    val orgGuid = uuid("org_guid")
    val title = text("title")
    val description = text("description").nullable()
}
