package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceQuestionTable
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceTable
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal class SqlFormInstanceMapper @Inject constructor(
    private val formInstanceQuestionStore: FormInstanceQuestionStore
) {

    fun formInstanceEntity(insertStatement: InsertStatement<*>, model: FormInstanceModel) {
        insertStatement[FormInstanceTable.createdDate] = model.created
        insertStatement[FormInstanceTable.guid] = model.id
        insertStatement[FormInstanceTable.orgGuid] = model.orgId
        insertStatement[FormInstanceTable.formTemplateGuid] = model.formTemplateId
    }

    fun formInstanceEntity(updateStatement: UpdateStatement, update: FormInstanceQuestionModel.Update) {
        when (update) {
            is FormInstanceDateQuestionModel.Update -> {
                update.date?.let { updateStatement[FormInstanceQuestionTable.date] = it }
            }
            is FormInstanceTextQuestionModel.Update -> {
                update.text?.let { updateStatement[FormInstanceQuestionTable.text] = it }
            }
            else -> error("Unexpected question type: ${update::class.qualifiedName}")
        }
    }

    fun formInstanceQuestionEntity(
        insertStatement: InsertStatement<*>,
        formInstanceId: UUID,
        model: FormInstanceQuestionModel
    ) {
        insertStatement[FormInstanceQuestionTable.createdDate] = model.created
        insertStatement[FormInstanceQuestionTable.formInstanceGuid] = formInstanceId
        insertStatement[FormInstanceQuestionTable.formTemplateQuestionGuid] = model.formTemplateQuestionId
        insertStatement[FormInstanceQuestionTable.type] = model.type.name
        when (model) {
            is FormInstanceDateQuestionModel -> {
                insertStatement[FormInstanceQuestionTable.date] = model.date
            }
            is FormInstanceTextQuestionModel -> {
                insertStatement[FormInstanceQuestionTable.text] = model.text
            }
            else -> error("Unexpected question type: ${model::class.qualifiedName}")
        }
    }

    fun formInstanceModel(resultRow: ResultRow): FormInstanceModel {
        val guid = resultRow[FormInstanceTable.guid]
        return FormInstanceModel(
            id = guid,
            created = resultRow[FormInstanceTable.createdDate],
            orgId = resultRow[FormInstanceTable.orgGuid],
            formTemplateId = resultRow[FormInstanceTable.formTemplateGuid],
            questions = formInstanceQuestionStore.getByFormInstanceId(guid)
        )
    }

    fun formInstanceQuestionModel(resultRow: ResultRow) =
        when (FormTemplateQuestionModel.Type.valueOf(resultRow[FormInstanceQuestionTable.type])) {
            FormTemplateQuestionModel.Type.DATE -> FormInstanceDateQuestionModel(
                created = resultRow[FormInstanceQuestionTable.createdDate],
                formTemplateQuestionId = resultRow[FormInstanceQuestionTable.formTemplateQuestionGuid],
                date = checkNotNull(resultRow[FormInstanceQuestionTable.date])
            )
            FormTemplateQuestionModel.Type.TEXT -> FormInstanceTextQuestionModel(
                created = resultRow[FormInstanceQuestionTable.createdDate],
                formTemplateQuestionId = resultRow[FormInstanceQuestionTable.formTemplateQuestionGuid],
                text = checkNotNull(resultRow[FormInstanceQuestionTable.text])
            )
        }
}
