package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.uuid.singleNullOrThrow
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
    private val sqlFormInstanceMapper: SqlFormInstanceMapper
) : FormInstanceStore, SqlStore(database) {

    override fun create(model: FormInstanceModel) = transaction {
        FormInstanceTable.insert { sqlFormInstanceMapper.formInstanceEntity(it, model) }
        formInstanceQuestionStore.create(model.id, model.questions.toSet())
    }

    override fun get(formInstanceId: UUID) = transaction {
        val entity = FormInstanceTable
            .select { FormInstanceTable.guid eq formInstanceId }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlFormInstanceMapper.formInstanceModel(entity)
    }

    override fun getByOrgId(orgId: UUID) = transaction {
        return@transaction (FormInstanceTable innerJoin FormTemplateTable)
            .select { FormTemplateTable.orgGuid eq orgId }
            .map { sqlFormInstanceMapper.formInstanceModel(it) }
            .toSet()
    }

    override fun delete(formInstanceId: UUID) = transaction<Unit> {
        FormInstanceTable
            .deleteExactlyOne(
                where = { FormInstanceTable.guid eq formInstanceId },
                notFound = { throw FormInstanceNotFound() }
            )
    }
}
