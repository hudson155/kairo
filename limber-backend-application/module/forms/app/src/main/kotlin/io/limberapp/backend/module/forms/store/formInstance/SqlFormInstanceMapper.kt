package io.limberapp.backend.module.forms.store.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal interface SqlFormInstanceMapper {

    fun formInstanceEntity(insertStatement: InsertStatement<*>, model: FormInstanceModel)

    fun formInstanceEntity(updateStatement: UpdateStatement, update: FormInstanceQuestionModel.Update)

    fun formInstanceQuestionEntity(
        insertStatement: InsertStatement<*>,
        formInstanceId: UUID,
        model: FormInstanceQuestionModel
    )

    fun formInstanceModel(resultRow: ResultRow): FormInstanceModel

    fun formInstanceQuestionModel(resultRow: ResultRow): FormInstanceQuestionModel
}
