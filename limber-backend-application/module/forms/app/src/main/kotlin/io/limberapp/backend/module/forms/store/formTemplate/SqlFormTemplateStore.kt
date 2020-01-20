package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateTable
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal class SqlFormTemplateStore @Inject constructor(
    database: Database,
    private val formTemplateQuestionStore: FormTemplateQuestionStore
) : FormTemplateStore, SqlStore(database) {

    override fun create(model: FormTemplateModel) = transaction {
        FormTemplateTable.insert { it.createFormTemplate(model) }
        formTemplateQuestionStore.create(model.id, model.questions)
    }

    private fun InsertStatement<*>.createFormTemplate(model: FormTemplateModel) {
        this[FormTemplateTable.createdDate] = model.created
        this[FormTemplateTable.guid] = model.id
        this[FormTemplateTable.orgGuid] = model.orgId
        this[FormTemplateTable.title] = model.title
        this[FormTemplateTable.description] = model.description
    }

    override fun get(formTemplateId: UUID) = transaction {
        return@transaction FormTemplateTable
            .select { FormTemplateTable.guid eq formTemplateId }
            .singleOrNull()
            ?.toFormTemplateModel()
    }

    override fun getByOrgId(orgId: UUID) = transaction {
        return@transaction FormTemplateTable
            .select { FormTemplateTable.orgGuid eq orgId }
            .map { it.toFormTemplateModel() }
    }

    override fun update(formTemplateId: UUID, update: FormTemplateModel.Update) = transaction {
        FormTemplateTable
            .updateAtMostOne(
                where = { FormTemplateTable.guid eq formTemplateId },
                body = { it.updateFormTemplate(update) }
            )
            .ifEq(0) { throw FormTemplateNotFound() }
        return@transaction checkNotNull(get(formTemplateId))
    }

    private fun UpdateStatement.updateFormTemplate(update: FormTemplateModel.Update) {
        update.title?.let { this[FormTemplateTable.title] = it }
        update.description?.let { this[FormTemplateTable.description] = it }
    }

    override fun delete(formTemplateId: UUID) = transaction<Unit> {
        FormTemplateTable
            .deleteAtMostOneWhere { FormTemplateTable.guid eq formTemplateId }
            .ifEq(0) { throw FormTemplateNotFound() }
    }

    private fun ResultRow.toFormTemplateModel(): FormTemplateModel {
        val guid = this[FormTemplateTable.guid]
        return FormTemplateModel(
            id = guid,
            created = this[FormTemplateTable.createdDate],
            orgId = this[FormTemplateTable.orgGuid],
            title = this[FormTemplateTable.title],
            description = this[FormTemplateTable.description],
            questions = formTemplateQuestionStore.getByFormTemplateId(guid)
        )
    }
}
