package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceQuestionTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionTable
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlFormInstanceQuestionStore @Inject constructor(
    database: Database,
    private val formInstanceMapper: FormInstanceMapper,
    private val sqlFormInstanceMapper: SqlFormInstanceMapper
) : FormInstanceQuestionStore, SqlStore(database) {

    override fun create(formInstanceId: UUID, models: Set<FormInstanceQuestionModel>) = transaction<Unit> {
        FormInstanceQuestionTable
            .batchInsert(models) { model ->
                sqlFormInstanceMapper.formInstanceQuestionEntity(this, formInstanceId, model)
            }
    }

    override fun upsert(formInstanceId: UUID, model: FormInstanceQuestionModel) = transaction {
        val formTemplateQuestionId = checkNotNull(model.formTemplateQuestionId)
        val existingFormInstanceQuestionModel = get(formInstanceId, formTemplateQuestionId)
        if (existingFormInstanceQuestionModel == null) {
            create(formInstanceId, model)
            return@transaction model
        } else {
            return@transaction update(formInstanceId, formTemplateQuestionId, formInstanceMapper.update(model))
        }
    }

    private fun create(formInstanceId: UUID, model: FormInstanceQuestionModel) = transaction {
        doOperationAndHandleErrors(
            operation = {
                FormInstanceQuestionTable
                    .insert { sqlFormInstanceMapper.formInstanceQuestionEntity(it, formInstanceId, model) }
            },
            onError = { error ->
                when {
                    error.isForeignKeyViolation(FormInstanceQuestionTable.formInstanceGuidForeignKey) ->
                        throw FormInstanceNotFound()
                    error.isForeignKeyViolation(FormInstanceQuestionTable.formTemplateQuestionGuidForeignKey) ->
                        throw FormTemplateQuestionNotFound()
                }
            }
        )
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

    private fun update(
        formInstanceId: UUID,
        formTemplateQuestionId: UUID,
        update: FormInstanceQuestionModel.Update
    ) = transaction {
        FormInstanceQuestionTable
            .updateExactlyOne(
                where = {
                    (FormInstanceQuestionTable.formInstanceGuid eq formInstanceId) and
                            (FormInstanceQuestionTable.formTemplateQuestionGuid eq formTemplateQuestionId)
                },
                body = { sqlFormInstanceMapper.formInstanceEntity(it, update) },
                notFound = { throw FormInstanceQuestionNotFound() }
            )
        return@transaction checkNotNull(get(formInstanceId, formTemplateQuestionId))
    }

    override fun delete(formInstanceId: UUID, formTemplateQuestionId: UUID) = transaction<Unit> {
        FormInstanceQuestionTable
            .deleteExactlyOne(
                where = {
                    (FormInstanceQuestionTable.formInstanceGuid eq formInstanceId) and
                            (FormInstanceQuestionTable.formTemplateQuestionGuid eq formTemplateQuestionId)
                },
                notFound = { throw FormInstanceQuestionNotFound() }
            )
    }
}
