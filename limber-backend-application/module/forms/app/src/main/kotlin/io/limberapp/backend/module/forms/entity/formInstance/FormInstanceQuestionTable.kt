package io.limberapp.backend.module.forms.entity.formInstance

import com.piperframework.sql.columnTypes.localDateTime.localdate
import com.piperframework.store.SqlTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionTable

object FormInstanceQuestionTable : SqlTable("forms", "form_instance_question") {
    val formInstanceGuid = uuid("form_instance_guid").references(FormInstanceTable.guid)
    val formTemplateQuestionGuid = uuid("form_template_question_guid")
        .references(FormTemplateQuestionTable.guid)
        .nullable()
    val type = text("type")
    val text = text("text").nullable()
    val date = localdate("date").nullable()
}
