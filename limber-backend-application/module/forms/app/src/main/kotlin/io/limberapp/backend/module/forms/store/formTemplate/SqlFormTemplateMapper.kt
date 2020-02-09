package io.limberapp.backend.module.forms.store.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

interface SqlFormTemplateMapper {

    fun formTemplateEntity(insertStatement: InsertStatement<*>, model: FormTemplateModel)

    fun formTemplateQuestionEntity(
        insertStatement: InsertStatement<*>,
        formTemplateId: UUID,
        model: FormTemplateQuestionModel,
        index: Int
    )

    fun formTemplateModel(resultRow: ResultRow): FormTemplateModel

    fun formTemplateQuestionModel(resultRow: ResultRow): FormTemplateQuestionModel
}
