package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateTable
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlFormInstanceStore @Inject constructor(
    database: Database,
    private val formInstanceQuestionStore: FormInstanceQuestionStore,
    private val mapper: SqlFormInstanceMapper
) : FormInstanceStore, SqlStore(database) {

    override fun create(model: FormInstanceModel) = transaction {
        FormInstanceTable.insert { mapper.formInstanceEntity(it, model) }
        formInstanceQuestionStore.create(model.id, model.questions)
    }

    override fun get(formInstanceId: UUID) = transaction {
        val entity = FormInstanceTable
            .select { FormInstanceTable.guid eq formInstanceId }
            .singleOrNull() ?: return@transaction null
        return@transaction mapper.formInstanceModel(entity)
    }

    override fun getByOrgId(orgId: UUID) = transaction {
        return@transaction (FormInstanceTable innerJoin FormTemplateTable)
            .select { FormTemplateTable.orgGuid eq orgId }
            .map { mapper.formInstanceModel(it) }
    }

    override fun delete(formInstanceId: UUID) = transaction<Unit> {
        FormInstanceTable
            .deleteAtMostOneWhere { FormInstanceTable.guid eq formInstanceId }
            .ifEq(0) { throw FormInstanceNotFound() }
    }
}
