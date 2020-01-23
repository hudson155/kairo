package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceQuestionTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionTable
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

internal class SqlFormInstanceQuestionStore @Inject constructor(
    database: Database
) : FormInstanceQuestionStore, SqlStore(database) {

    override fun create(formInstanceId: UUID, models: List<FormInstanceQuestionModel>) = transaction<Unit> {
        FormInstanceQuestionTable
            .batchInsert(models) { model -> createFormInstanceQuestion(formInstanceId, model) }
    }

    override fun createOrUpdate(formInstanceId: UUID, model: FormInstanceQuestionModel) = transaction<Unit> {
        TODO()
    }

    private fun InsertStatement<*>.createFormInstanceQuestion(formInstanceId: UUID, model: FormInstanceQuestionModel) {
        this[FormInstanceQuestionTable.createdDate] = model.created
        this[FormInstanceQuestionTable.formInstanceGuid] = formInstanceId
        this[FormInstanceQuestionTable.formTemplateQuestionGuid] = model.formTemplateQuestionId
        this[FormInstanceQuestionTable.type] = model.type.name
        when (model) {
            is FormInstanceDateQuestionModel -> {
                this[FormInstanceQuestionTable.date] = model.date
            }
            is FormInstanceTextQuestionModel -> {
                this[FormInstanceQuestionTable.text] = model.text
            }
            else -> error("Unexpected question type: ${model::class.qualifiedName}")
        }
    }

    override fun get(formInstanceId: UUID, formTemplateQuestionId: UUID) = transaction {
        return@transaction FormInstanceQuestionTable
            .select {
                (FormInstanceQuestionTable.formInstanceGuid eq formInstanceId) and
                        (FormInstanceQuestionTable.formTemplateQuestionGuid eq formTemplateQuestionId)
            }
            .singleOrNull()
            ?.toFormInstanceQuestionModel()
    }

    override fun getByFormInstanceId(formInstanceId: UUID) = transaction {
        return@transaction (FormInstanceQuestionTable innerJoin FormTemplateQuestionTable)
            .select { FormInstanceQuestionTable.formInstanceGuid eq formInstanceId }
            .orderBy(FormTemplateQuestionTable.rank)
            .map { it.toFormInstanceQuestionModel() }
    }

    override fun delete(formInstanceId: UUID, formTemplateQuestionId: UUID) = transaction<Unit> {
        FormInstanceQuestionTable
            .deleteAtMostOneWhere {
                (FormInstanceQuestionTable.formInstanceGuid eq formInstanceId) and
                        (FormTemplateQuestionTable.guid eq formTemplateQuestionId)
            }
            .ifEq(0) { throw FormInstanceQuestionNotFound() }
    }

    private fun ResultRow.toFormInstanceQuestionModel() =
        when (FormInstanceQuestionModel.Type.valueOf(this[FormInstanceQuestionTable.type])) {
            FormInstanceQuestionModel.Type.DATE -> FormInstanceDateQuestionModel(
                created = this[FormInstanceQuestionTable.createdDate],
                formTemplateQuestionId = this[FormInstanceQuestionTable.formTemplateQuestionGuid],
                date = checkNotNull(this[FormInstanceQuestionTable.date])
            )
            FormInstanceQuestionModel.Type.TEXT -> FormInstanceTextQuestionModel(
                created = this[FormInstanceQuestionTable.createdDate],
                formTemplateQuestionId = this[FormInstanceQuestionTable.formTemplateQuestionGuid],
                text = checkNotNull(this[FormInstanceQuestionTable.text])
            )
        }
}
