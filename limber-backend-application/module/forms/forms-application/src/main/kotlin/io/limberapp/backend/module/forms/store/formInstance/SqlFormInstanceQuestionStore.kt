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
    override fun create(formInstanceGuid: UUID, models: Set<FormInstanceQuestionModel>) = transaction<Unit> {
        FormInstanceQuestionTable
            .batchInsert(models) { model ->
                sqlFormInstanceMapper.formInstanceQuestionEntity(this, formInstanceGuid, model)
            }
    }

    override fun upsert(formInstanceGuid: UUID, model: FormInstanceQuestionModel) = transaction {
        val questionGuid = checkNotNull(model.questionGuid)
        val existingFormInstanceQuestionModel = get(formInstanceGuid, questionGuid)
        if (existingFormInstanceQuestionModel == null) {
            create(formInstanceGuid, model)
            return@transaction model
        } else {
            return@transaction update(formInstanceGuid, questionGuid, formInstanceQuestionMapper.update(model))
        }
    }

    private fun create(formInstanceGuid: UUID, model: FormInstanceQuestionModel) = transaction {
        doOperation {
            FormInstanceQuestionTable
                .insert { sqlFormInstanceMapper.formInstanceQuestionEntity(it, formInstanceGuid, model) }
        } andHandleError {
            when {
                error.isForeignKeyViolation(FormInstanceQuestionTable.formInstanceGuidForeignKey) ->
                    throw FormInstanceNotFound()
                error.isForeignKeyViolation(FormInstanceQuestionTable.formTemplateQuestionGuidForeignKey) ->
                    throw FormTemplateQuestionNotFound()
            }
        }
    }

    override fun get(formInstanceGuid: UUID, questionGuid: UUID) = transaction {
        val entity = FormInstanceQuestionTable
            .select {
                (FormInstanceQuestionTable.formInstanceGuid eq formInstanceGuid) and
                        (FormInstanceQuestionTable.formTemplateQuestionGuid eq questionGuid)
            }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlFormInstanceMapper.formInstanceQuestionModel(entity)
    }

    override fun getByFormInstanceGuid(formInstanceGuid: UUID) = transaction {
        return@transaction (FormInstanceQuestionTable innerJoin FormTemplateQuestionTable)
            .select { FormInstanceQuestionTable.formInstanceGuid eq formInstanceGuid }
            .orderBy(FormTemplateQuestionTable.rank)
            .map { sqlFormInstanceMapper.formInstanceQuestionModel(it) }
    }

    private fun update(
        formInstanceGuid: UUID,
        questionGuid: UUID,
        update: FormInstanceQuestionModel.Update
    ) = transaction {
        FormInstanceQuestionTable
            .updateExactlyOne(
                where = {
                    (FormInstanceQuestionTable.formInstanceGuid eq formInstanceGuid) and
                            (FormInstanceQuestionTable.formTemplateQuestionGuid eq questionGuid)
                },
                body = { sqlFormInstanceMapper.formInstanceEntity(it, update) },
                notFound = { throw FormInstanceQuestionNotFound() }
            )
        return@transaction checkNotNull(get(formInstanceGuid, questionGuid))
    }

    override fun delete(formInstanceGuid: UUID, questionGuid: UUID) = transaction<Unit> {
        FormInstanceQuestionTable
            .deleteExactlyOne(
                where = {
                    (FormInstanceQuestionTable.formInstanceGuid eq formInstanceGuid) and
                            (FormInstanceQuestionTable.formTemplateQuestionGuid eq questionGuid)
                },
                notFound = { throw FormInstanceQuestionNotFound() }
            )
    }
}
