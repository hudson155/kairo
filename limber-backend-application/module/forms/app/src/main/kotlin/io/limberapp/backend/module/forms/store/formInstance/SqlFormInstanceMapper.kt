package io.limberapp.backend.module.forms.store.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

interface SqlFormInstanceMapper {

    fun formInstanceEntity(insertStatement: InsertStatement<*>, model: FormInstanceModel)

    fun formInstanceQuestionEntity(
        insertStatement: InsertStatement<*>,
        formInstanceId: UUID,
        model: FormInstanceQuestionModel
    )

    fun formInstanceModel(resultRow: ResultRow): FormInstanceModel

    fun formInstanceQuestionModel(resultRow: ResultRow): FormInstanceQuestionModel
}
