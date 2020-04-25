package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
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
        formInstanceQuestionStore.create(model.guid, model.questions.toSet())
    }

    override fun get(formInstanceGuid: UUID) = transaction {
        val entity = FormInstanceTable
            .select { FormInstanceTable.guid eq formInstanceGuid }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlFormInstanceMapper.formInstanceModel(entity)
    }

    override fun getByFeatureGuid(featureGuid: UUID) = transaction {
        return@transaction (FormInstanceTable innerJoin FormTemplateTable)
            .select { FormTemplateTable.featureGuid eq featureGuid }
            .map { sqlFormInstanceMapper.formInstanceModel(it) }
            .toSet()
    }

    override fun delete(formInstanceGuid: UUID) = transaction<Unit> {
        FormInstanceTable
            .deleteExactlyOne(
                where = { FormInstanceTable.guid eq formInstanceGuid },
                notFound = { throw FormInstanceNotFound() }
            )
    }
}
