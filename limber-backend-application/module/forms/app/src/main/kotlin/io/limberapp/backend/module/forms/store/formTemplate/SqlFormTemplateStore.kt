package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateTable
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.util.UUID

internal class SqlFormTemplateStore @Inject constructor(
    database: Database,
    private val formTemplateQuestionStore: FormTemplateQuestionStore,
    private val sqlFormTemplateMapper: SqlFormTemplateMapper
) : FormTemplateStore, SqlStore(database) {

    override fun create(model: FormTemplateModel) = transaction {
        FormTemplateTable.insert { sqlFormTemplateMapper.formTemplateEntity(it, model) }
        formTemplateQuestionStore.create(model.id, model.questions)
    }

    override fun get(formTemplateId: UUID) = transaction {
        val entity = FormTemplateTable
            .select { FormTemplateTable.guid eq formTemplateId }
            .singleOrNull() ?: return@transaction null
        return@transaction sqlFormTemplateMapper.formTemplateModel(entity)
    }

    override fun getByOrgId(orgId: UUID) = transaction {
        return@transaction FormTemplateTable
            .select { FormTemplateTable.orgGuid eq orgId }
            .map { sqlFormTemplateMapper.formTemplateModel(it) }
    }

    override fun update(formTemplateId: UUID, update: FormTemplateModel.Update) = transaction {
        FormTemplateTable
            .updateExactlyOne(
                where = { FormTemplateTable.guid eq formTemplateId },
                body = { it.updateFormTemplate(update) },
                notFound = { throw FormTemplateNotFound() }
            )
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
}
