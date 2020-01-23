package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateTable
import io.limberapp.backend.module.forms.exception.notFound.FormInstanceNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

internal class SqlFormInstanceStore @Inject constructor(
    database: Database,
    private val formInstanceQuestionStore: FormInstanceQuestionStore
) : FormInstanceStore, SqlStore(database) {

    override fun create(model: FormInstanceModel) = transaction {
        FormInstanceTable.insert { it.createFormInstance(model) }
        formInstanceQuestionStore.create(model.id, model.questions)
    }

    private fun InsertStatement<*>.createFormInstance(model: FormInstanceModel) {
        this[FormInstanceTable.createdDate] = model.created
        this[FormInstanceTable.guid] = model.id
        this[FormInstanceTable.formTemplateGuid] = model.formTemplateId
    }

    override fun get(formInstanceId: UUID) = transaction {
        return@transaction FormInstanceTable
            .select { FormInstanceTable.guid eq formInstanceId }
            .singleOrNull()
            ?.toFormInstanceModel()
    }

    override fun getByOrgId(orgId: UUID) = transaction {
        return@transaction (FormInstanceTable innerJoin FormTemplateTable)
            .select { FormTemplateTable.orgGuid eq orgId }
            .map { it.toFormInstanceModel() }
    }

    override fun delete(formInstanceId: UUID) = transaction<Unit> {
        FormInstanceTable
            .deleteAtMostOneWhere { FormInstanceTable.guid eq formInstanceId }
            .ifEq(0) { throw FormInstanceNotFound() }
    }

    private fun ResultRow.toFormInstanceModel(): FormInstanceModel {
        val guid = this[FormInstanceTable.guid]
        return FormInstanceModel(
            id = guid,
            created = this[FormInstanceTable.createdDate],
            formTemplateId = this[FormInstanceTable.formTemplateGuid],
            questions = formInstanceQuestionStore.getByFormInstanceId(guid)
        )
    }
}
