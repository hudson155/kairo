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
        formTemplateQuestionStore.create(model.id, model.questions)
    }

    override fun get(formTemplateId: UUID) = transaction {
        val entity = FormTemplateTable
            .select { FormTemplateTable.guid eq formTemplateId }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlFormTemplateMapper.formTemplateModel(entity)
    }

    override fun getByFeatureId(featureId: UUID) = transaction {
        return@transaction FormTemplateTable
            .select { FormTemplateTable.featureGuid eq featureId }
            .map { sqlFormTemplateMapper.formTemplateModel(it) }
            .toSet()
    }

    override fun update(formTemplateId: UUID, update: FormTemplateModel.Update) = transaction {
        FormTemplateTable
            .updateExactlyOne(
                where = { FormTemplateTable.guid eq formTemplateId },
                body = { sqlFormTemplateMapper.formTemplateEntity(it, update) },
                notFound = { throw FormTemplateNotFound() }
            )
        return@transaction checkNotNull(get(formTemplateId))
    }

    override fun delete(formTemplateId: UUID) = transaction<Unit> {
        FormTemplateTable
            .deleteExactlyOne(
                where = { FormTemplateTable.guid eq formTemplateId },
                notFound = { throw FormTemplateNotFound() }
            )
    }
}
