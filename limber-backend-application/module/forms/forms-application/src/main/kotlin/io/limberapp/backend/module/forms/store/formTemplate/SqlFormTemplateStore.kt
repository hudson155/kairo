package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateTable
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlFormTemplateStore @Inject constructor(
    database: Database,
    private val formTemplateQuestionStore: FormTemplateQuestionStore,
    private val sqlFormTemplateMapper: SqlFormTemplateMapper
) : FormTemplateStore, SqlStore(database) {
    override fun create(model: FormTemplateModel) = transaction {
        FormTemplateTable.insert { sqlFormTemplateMapper.formTemplateEntity(it, model) }
        formTemplateQuestionStore.create(model.guid, model.questions)
    }

    override fun get(formTemplateGuid: UUID) = transaction {
        val entity = FormTemplateTable
            .select { FormTemplateTable.guid eq formTemplateGuid }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlFormTemplateMapper.formTemplateModel(entity)
    }

    override fun getByFeatureGuid(featureGuid: UUID) = transaction {
        return@transaction FormTemplateTable
            .select { FormTemplateTable.featureGuid eq featureGuid }
            .map { sqlFormTemplateMapper.formTemplateModel(it) }
            .toSet()
    }

    override fun update(formTemplateGuid: UUID, update: FormTemplateModel.Update) = transaction {
        FormTemplateTable
            .updateExactlyOne(
                where = { FormTemplateTable.guid eq formTemplateGuid },
                body = { sqlFormTemplateMapper.formTemplateEntity(it, update) },
                notFound = { throw FormTemplateNotFound() }
            )
        return@transaction checkNotNull(get(formTemplateGuid))
    }

    override fun delete(formTemplateGuid: UUID) = transaction<Unit> {
        FormTemplateTable
            .deleteExactlyOne(
                where = { FormTemplateTable.guid eq formTemplateGuid },
                notFound = { throw FormTemplateNotFound() }
            )
    }
}
