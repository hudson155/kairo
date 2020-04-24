package io.limberapp.backend.module.forms.store.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal interface SqlFormTemplateMapper {
    fun formTemplateEntity(insertStatement: InsertStatement<*>, model: FormTemplateModel)

    fun formTemplateEntity(updateStatement: UpdateStatement, update: FormTemplateModel.Update)

    fun formTemplateQuestionEntity(
        insertStatement: InsertStatement<*>,
        formTemplateId: UUID,
        model: FormTemplateQuestionModel,
        rank: Int
    )

    fun formTemplateQuestionEntity(updateStatement: UpdateStatement, update: FormTemplateQuestionModel.Update)

    fun formTemplateModel(resultRow: ResultRow): FormTemplateModel

    fun formTemplateQuestionModel(resultRow: ResultRow): FormTemplateQuestionModel
}
