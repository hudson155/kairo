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
        insertStatement[FormInstanceTable.createdDate] = model.createdDate
        insertStatement[FormInstanceTable.guid] = model.guid
        insertStatement[FormInstanceTable.featureGuid] = model.featureGuid
        insertStatement[FormInstanceTable.formTemplateGuid] = model.formTemplateGuid
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
        formInstanceGuid: UUID,
        model: FormInstanceQuestionModel
    ) {
        insertStatement[FormInstanceQuestionTable.createdDate] = model.createdDate
        insertStatement[FormInstanceQuestionTable.formInstanceGuid] = formInstanceGuid
        insertStatement[FormInstanceQuestionTable.formTemplateQuestionGuid] = model.questionGuid
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
            guid = guid,
            createdDate = resultRow[FormInstanceTable.createdDate],
            featureGuid = resultRow[FormInstanceTable.featureGuid],
            formTemplateGuid = resultRow[FormInstanceTable.formTemplateGuid],
            questions = formInstanceQuestionStore.getByFormInstanceGuid(guid)
        )
    }

    override fun formInstanceQuestionModel(resultRow: ResultRow) =
        when (FormTemplateQuestionModel.Type.valueOf(resultRow[FormInstanceQuestionTable.type])) {
            FormTemplateQuestionModel.Type.DATE -> FormInstanceDateQuestionModel(
                createdDate = resultRow[FormInstanceQuestionTable.createdDate],
                questionGuid = resultRow[FormInstanceQuestionTable.formTemplateQuestionGuid],
                date = checkNotNull(resultRow[FormInstanceQuestionTable.date])
            )
            FormTemplateQuestionModel.Type.RADIO_SELECTOR -> FormInstanceRadioSelectorQuestionModel(
                createdDate = resultRow[FormInstanceQuestionTable.createdDate],
                questionGuid = resultRow[FormInstanceQuestionTable.formTemplateQuestionGuid],
                selection = checkNotNull(resultRow[FormInstanceQuestionTable.selections]).single()
            )
            FormTemplateQuestionModel.Type.TEXT -> FormInstanceTextQuestionModel(
                createdDate = resultRow[FormInstanceQuestionTable.createdDate],
                questionGuid = resultRow[FormInstanceQuestionTable.formTemplateQuestionGuid],
                text = checkNotNull(resultRow[FormInstanceQuestionTable.text])
            )
        }

    private fun unknownFormInstanceQuestion(kClass: KClass<*>) {
        unknownType("form instance question", kClass::class)
    }
}
