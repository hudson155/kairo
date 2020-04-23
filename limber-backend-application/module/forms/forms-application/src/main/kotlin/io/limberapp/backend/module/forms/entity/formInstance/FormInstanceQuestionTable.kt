package io.limberapp.backend.module.forms.entity.formInstance

import com.piperframework.sql.columnTypes.localDate
import com.piperframework.sql.columnTypes.stringList
import com.piperframework.store.SqlTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionTable

internal object FormInstanceQuestionTable : SqlTable("forms", "form_instance_question") {

    val formInstanceGuid = uuid("form_instance_guid").references(FormInstanceTable.guid)
    const val formInstanceGuidForeignKey = "form_instance_question_form_instance_guid_fkey"

    val formTemplateQuestionGuid = uuid("form_template_question_guid")
        .references(FormTemplateQuestionTable.guid)
        .nullable()
    const val formTemplateQuestionGuidForeignKey = "form_instance_question_form_template_question_guid_fkey"

    val type = text("type")

    val text = text("text").nullable()

    val date = localDate("date").nullable()

    val selections = stringList("selections").nullable()
}
