package io.limberapp.backend.module.forms.entity.formTemplate

import com.piperframework.sql.columnTypes.localDate
import com.piperframework.sql.columnTypes.stringList
import com.piperframework.store.SqlTable

internal object FormTemplateQuestionTable : SqlTable("forms", "form_template_question") {

    val guid = uuid("guid")

    val formTemplateGuid = uuid("form_template_guid")

    val rank = integer("rank")

    val label = text("label")

    val helpText = text("help_text").nullable()

    val type = text("type")

    val multiLine = bool("multi_line").nullable()

    val placeholder = text("placeholder").nullable()

    val validator = text("validator").nullable()

    val earliest = localDate("earliest").nullable()

    val latest = localDate("latest").nullable()

    val options = stringList("options").nullable()
}
