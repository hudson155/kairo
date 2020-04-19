package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceQuestionTable
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionTable
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceQuestionMapper
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal class SqlFormInstanceQuestionStore @Inject constructor(
    database: Database,
    private val formInstanceQuestionMapper: FormInstanceQuestionMapper,
    private val sqlFormInstanceMapper: SqlFormInstanceMapper
) : FormInstanceQuestionStore, SqlStore(database) {

    override fun create(formInstanceId: UUID, models: Set<FormInstanceQuestionModel>) = transaction<Unit> {
        FormInstanceQuestionTable
            .batchInsert(models) { model ->
                sqlFormInstanceMapper.formInstanceQuestionEntity(this, formInstanceId, model)
            }
    }

    override fun upsert(formInstanceId: UUID, model: FormInstanceQuestionModel) = transaction {
        val questionId = checkNotNull(model.questionId)
        val existingFormInstanceQuestionModel = get(formInstanceId, questionId)
        if (existingFormInstanceQuestionModel == null) {
            create(formInstanceId, model)
            return@transaction model
        } else {
            return@transaction update(formInstanceId, questionId, formInstanceQuestionMapper.update(model))
        }
    }

    private fun create(formInstanceId: UUID, model: FormInstanceQuestionModel) = transaction {
        doOperation {
            FormInstanceQuestionTable
                .insert { sqlFormInstanceMapper.formInstanceQuestionEntity(it, formInstanceId, model) }
        } andHandleError {
            when {
                error.isForeignKeyViolation(FormInstanceQuestionTable.formInstanceGuidForeignKey) ->
                    throw FormInstanceNotFound()
                error.isForeignKeyViolation(FormInstanceQuestionTable.formTemplateQuestionGuidForeignKey) ->
                    throw FormTemplateQuestionNotFound()
            }
        }
    }

    override fun get(formInstanceId: UUID, questionId: UUID) = transaction {
        val entity = FormInstanceQuestionTable
            .select {
                (FormInstanceQuestionTable.formInstanceGuid eq formInstanceId) and
                        (FormInstanceQuestionTable.formTemplateQuestionGuid eq questionId)
            }
            .singleNullOrThrow() ?: return@transaction null
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
        questionId: UUID,
        update: FormInstanceQuestionModel.Update
    ) = transaction {
        FormInstanceQuestionTable
            .updateExactlyOne(
                where = {
                    (FormInstanceQuestionTable.formInstanceGuid eq formInstanceId) and
                            (FormInstanceQuestionTable.formTemplateQuestionGuid eq questionId)
                },
                body = { sqlFormInstanceMapper.formInstanceEntity(it, update) },
                notFound = { throw FormInstanceQuestionNotFound() }
            )
        return@transaction checkNotNull(get(formInstanceId, questionId))
    }

    override fun delete(formInstanceId: UUID, questionId: UUID) = transaction<Unit> {
        FormInstanceQuestionTable
            .deleteExactlyOne(
                where = {
                    (FormInstanceQuestionTable.formInstanceGuid eq formInstanceId) and
                            (FormInstanceQuestionTable.formTemplateQuestionGuid eq questionId)
                },
                notFound = { throw FormInstanceQuestionNotFound() }
            )
    }
}
