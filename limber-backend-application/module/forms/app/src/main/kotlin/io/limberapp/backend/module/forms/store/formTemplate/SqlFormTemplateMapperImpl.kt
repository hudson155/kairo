package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateTable
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

internal class SqlFormTemplateMapperImpl @Inject constructor(
    private val formTemplateQuestionStore: FormTemplateQuestionStore
) : SqlFormTemplateMapper {

    override fun formTemplateEntity(insertStatement: InsertStatement<*>, model: FormTemplateModel) {
        insertStatement[FormTemplateTable.createdDate] = model.created
        insertStatement[FormTemplateTable.guid] = model.id
        insertStatement[FormTemplateTable.orgGuid] = model.orgId
        insertStatement[FormTemplateTable.title] = model.title
        insertStatement[FormTemplateTable.description] = model.description
    }

    override fun formTemplateQuestionEntity(
        insertStatement: InsertStatement<*>,
        formTemplateId: UUID,
        model: FormTemplateQuestionModel,
        index: Int
    ) {
        insertStatement[FormTemplateQuestionTable.createdDate] = model.created
        insertStatement[FormTemplateQuestionTable.guid] = model.id
        insertStatement[FormTemplateQuestionTable.formTemplateGuid] = formTemplateId
        insertStatement[FormTemplateQuestionTable.rank] = index
        insertStatement[FormTemplateQuestionTable.label] = model.label
        insertStatement[FormTemplateQuestionTable.helpText] = model.helpText
        insertStatement[FormTemplateQuestionTable.type] = model.type.name
        when (model) {
            is FormTemplateDateQuestionModel -> {
                insertStatement[FormTemplateQuestionTable.earliest] = model.earliest
                insertStatement[FormTemplateQuestionTable.latest] = model.latest
            }
            is FormTemplateTextQuestionModel -> {
                insertStatement[FormTemplateQuestionTable.multiLine] = model.multiLine
                insertStatement[FormTemplateQuestionTable.placeholder] = model.placeholder
                insertStatement[FormTemplateQuestionTable.validator] = model.validator?.pattern
            }
            else -> error("Unexpected question type: ${model::class.qualifiedName}")
        }
    }

    override fun formTemplateModel(resultRow: ResultRow): FormTemplateModel {
        val guid = resultRow[FormTemplateTable.guid]
        return FormTemplateModel(
            id = guid,
            created = resultRow[FormTemplateTable.createdDate],
            orgId = resultRow[FormTemplateTable.orgGuid],
            title = resultRow[FormTemplateTable.title],
            description = resultRow[FormTemplateTable.description],
            questions = formTemplateQuestionStore.getByFormTemplateId(guid)
        )
    }

    override fun formTemplateQuestionModel(resultRow: ResultRow) =
        when (FormTemplateQuestionModel.Type.valueOf(resultRow[FormTemplateQuestionTable.type])) {
            FormTemplateQuestionModel.Type.DATE -> FormTemplateDateQuestionModel(
                id = resultRow[FormTemplateQuestionTable.guid],
                created = resultRow[FormTemplateQuestionTable.createdDate],
                label = resultRow[FormTemplateQuestionTable.label],
                helpText = resultRow[FormTemplateQuestionTable.helpText],
                earliest = resultRow[FormTemplateQuestionTable.earliest],
                latest = resultRow[FormTemplateQuestionTable.latest]
            )
            FormTemplateQuestionModel.Type.TEXT -> FormTemplateTextQuestionModel(
                id = resultRow[FormTemplateQuestionTable.guid],
                created = resultRow[FormTemplateQuestionTable.createdDate],
                label = resultRow[FormTemplateQuestionTable.label],
                helpText = resultRow[FormTemplateQuestionTable.helpText],
                multiLine = checkNotNull(resultRow[FormTemplateQuestionTable.multiLine]),
                placeholder = resultRow[FormTemplateQuestionTable.placeholder],
                validator = resultRow[FormTemplateQuestionTable.validator]?.let { Regex(it) }
            )
        }
}
