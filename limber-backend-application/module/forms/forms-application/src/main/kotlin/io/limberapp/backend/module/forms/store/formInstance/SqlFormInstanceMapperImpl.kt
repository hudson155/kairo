package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.util.unknownType
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceQuestionTable
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceTable
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID
import kotlin.reflect.KClass

internal class SqlFormInstanceMapperImpl @Inject constructor(
    private val formInstanceQuestionStore: FormInstanceQuestionStore
) : SqlFormInstanceMapper {

    override fun formInstanceEntity(insertStatement: InsertStatement<*>, model: FormInstanceModel) {
        insertStatement[FormInstanceTable.createdDate] = model.created
        insertStatement[FormInstanceTable.guid] = model.id
        insertStatement[FormInstanceTable.featureGuid] = model.featureId
        insertStatement[FormInstanceTable.formTemplateGuid] = model.formTemplateId
    }

    override fun formInstanceEntity(updateStatement: UpdateStatement, update: FormInstanceQuestionModel.Update) {
        when (update) {
            is FormInstanceDateQuestionModel.Update -> {
                update.date?.let { updateStatement[FormInstanceQuestionTable.date] = it }
            }
            is FormInstanceRadioSelectorQuestionModel.Update -> {
                update.selection?.let { updateStatement[FormInstanceQuestionTable.selections] = arrayOf(it) }
            }
            is FormInstanceTextQuestionModel.Update -> {
                update.text?.let { updateStatement[FormInstanceQuestionTable.text] = it }
            }
            else -> unknownFormInstanceQuestion(update::class)
        }
    }

    override fun formInstanceQuestionEntity(
        insertStatement: InsertStatement<*>,
        formInstanceId: UUID,
        model: FormInstanceQuestionModel
    ) {
        insertStatement[FormInstanceQuestionTable.createdDate] = model.created
        insertStatement[FormInstanceQuestionTable.formInstanceGuid] = formInstanceId
        insertStatement[FormInstanceQuestionTable.formTemplateQuestionGuid] = model.questionId
        insertStatement[FormInstanceQuestionTable.type] = model.type.name
        when (model) {
            is FormInstanceDateQuestionModel -> {
                insertStatement[FormInstanceQuestionTable.date] = model.date
            }
            is FormInstanceRadioSelectorQuestionModel -> {
                insertStatement[FormInstanceQuestionTable.selections] = arrayOf(model.selection)
            }
            is FormInstanceTextQuestionModel -> {
                insertStatement[FormInstanceQuestionTable.text] = model.text
            }
            else -> unknownFormInstanceQuestion(model::class)
        }
    }

    override fun formInstanceModel(resultRow: ResultRow): FormInstanceModel {
        val guid = resultRow[FormInstanceTable.guid]
        return FormInstanceModel(
            id = guid,
            created = resultRow[FormInstanceTable.createdDate],
            featureId = resultRow[FormInstanceTable.featureGuid],
            formTemplateId = resultRow[FormInstanceTable.formTemplateGuid],
            questions = formInstanceQuestionStore.getByFormInstanceId(guid)
        )
    }

    override fun formInstanceQuestionModel(resultRow: ResultRow) =
        when (FormTemplateQuestionModel.Type.valueOf(resultRow[FormInstanceQuestionTable.type])) {
            FormTemplateQuestionModel.Type.DATE -> FormInstanceDateQuestionModel(
                created = resultRow[FormInstanceQuestionTable.createdDate],
                questionId = resultRow[FormInstanceQuestionTable.formTemplateQuestionGuid],
                date = checkNotNull(resultRow[FormInstanceQuestionTable.date])
            )
            FormTemplateQuestionModel.Type.RADIO_SELECTOR -> FormInstanceRadioSelectorQuestionModel(
                created = resultRow[FormInstanceQuestionTable.createdDate],
                questionId = resultRow[FormInstanceQuestionTable.formTemplateQuestionGuid],
                selection = checkNotNull(resultRow[FormInstanceQuestionTable.selections]).single()
            )
            FormTemplateQuestionModel.Type.TEXT -> FormInstanceTextQuestionModel(
                created = resultRow[FormInstanceQuestionTable.createdDate],
                questionId = resultRow[FormInstanceQuestionTable.formTemplateQuestionGuid],
                text = checkNotNull(resultRow[FormInstanceQuestionTable.text])
            )
        }

    private fun unknownFormInstanceQuestion(kClass: KClass<*>) {
        unknownType("form instance question", kClass::class)
    }
}
