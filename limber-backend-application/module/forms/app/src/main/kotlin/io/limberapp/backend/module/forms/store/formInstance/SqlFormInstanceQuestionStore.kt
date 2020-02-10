package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceQuestionTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionTable
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlFormInstanceQuestionStore @Inject constructor(
    database: Database,
    private val sqlFormInstanceMapper: SqlFormInstanceMapper
) : FormInstanceQuestionStore, SqlStore(database) {

    override fun create(formInstanceId: UUID, models: List<FormInstanceQuestionModel>) = transaction<Unit> {
        FormInstanceQuestionTable
            .batchInsert(models) { model ->
                sqlFormInstanceMapper.formInstanceQuestionEntity(this, formInstanceId, model)
            }
    }

    override fun get(formInstanceId: UUID, formTemplateQuestionId: UUID) = transaction {
        val entity = FormInstanceQuestionTable
            .select {
                (FormInstanceQuestionTable.formInstanceGuid eq formInstanceId) and
                        (FormInstanceQuestionTable.formTemplateQuestionGuid eq formTemplateQuestionId)
            }
            .singleOrNull() ?: return@transaction null
        return@transaction sqlFormInstanceMapper.formInstanceQuestionModel(entity)
    }

    override fun getByFormInstanceId(formInstanceId: UUID) = transaction {
        return@transaction (FormInstanceQuestionTable innerJoin FormTemplateQuestionTable)
            .select { FormInstanceQuestionTable.formInstanceGuid eq formInstanceId }
            .orderBy(FormTemplateQuestionTable.rank)
            .map { sqlFormInstanceMapper.formInstanceQuestionModel(it) }
    }

    override fun delete(formInstanceId: UUID, formTemplateQuestionId: UUID) = transaction<Unit> {
        FormInstanceQuestionTable
            .deleteExactlyOne(
                where = {
                    (FormInstanceQuestionTable.formInstanceGuid eq formInstanceId) and
                            (FormTemplateQuestionTable.guid eq formTemplateQuestionId)
                },
                notFound = { throw FormInstanceQuestionNotFound() }
            )
    }
}
