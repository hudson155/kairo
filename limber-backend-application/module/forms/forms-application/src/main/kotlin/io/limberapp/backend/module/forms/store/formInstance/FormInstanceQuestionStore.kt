package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.forms.entity.formInstance.FormInstanceQuestionEntity
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.UUID

private const val FORM_INSTANCE_GUID_FOREIGN_KEY = "form_instance_question_form_instance_guid_fkey"
private const val QUESTION_GUID_FOREIGN_KEY = "form_instance_question_question_guid_fkey"

internal class FormInstanceQuestionStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
    fun create(formInstanceGuid: UUID, models: Set<FormInstanceQuestionModel>) {
        jdbi.useTransaction<Exception> {
            try {
                it.prepareBatch(sqlResource("create")).apply {
                    models.forEach {
                        this
                            .bind("formInstanceGuid", formInstanceGuid)
                            .bindKotlin(FormInstanceQuestionEntity(it))
                            .add()
                    }
                }.execute()
            } catch (e: UnableToExecuteStatementException) {
                handleCreateError(e)
            }
        }
    }

    fun create(formInstanceGuid: UUID, model: FormInstanceQuestionModel) {
        jdbi.useTransaction<Exception> {
            try {
                it.createUpdate(sqlResource("create"))
                    .bind("formInstanceGuid", formInstanceGuid)
                    .bindKotlin(FormInstanceQuestionEntity(model))
                    .execute()
            } catch (e: UnableToExecuteStatementException) {
                handleCreateError(e)
            }
        }
    }

    private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
        val error = e.serverErrorMessage ?: throw e
        when {
            error.isForeignKeyViolation(FORM_INSTANCE_GUID_FOREIGN_KEY) ->
                throw FormInstanceNotFound()
            error.isForeignKeyViolation(QUESTION_GUID_FOREIGN_KEY) ->
                throw FormTemplateQuestionNotFound()
            else -> throw e
        }
    }

    fun get(formInstanceGuid: UUID, questionGuid: UUID): FormInstanceQuestionModel? {
        return jdbi.withHandle<FormInstanceQuestionModel, Exception> {
            it.createQuery(sqlResource("getByFormInstanceGuid"))
                .bind("formInstanceGuid", formInstanceGuid)
                .bind("questionGuid", questionGuid)
                .mapTo(FormInstanceQuestionEntity::class.java)
                .singleNullOrThrow()
                ?.asModel()
        }
    }

    fun getByFormInstanceGuid(formInstanceGuid: UUID): List<FormInstanceQuestionModel> {
        return jdbi.withHandle<List<FormInstanceQuestionModel>, Exception> {
            it.createQuery(sqlResource("getByFormInstanceGuid"))
                .bind("formInstanceGuid", formInstanceGuid)
                .mapTo(FormInstanceQuestionEntity::class.java)
                .map { it.asModel() }
                .toList()
        }
    }

    fun update(
        formInstanceGuid: UUID,
        questionGuid: UUID,
        update: FormInstanceQuestionModel.Update
    ): FormInstanceQuestionModel {
        return jdbi.inTransaction<FormInstanceQuestionModel, Exception> {
            val updateCount = it.createUpdate(sqlResource("update"))
                .bind("formInstanceGuid", formInstanceGuid)
                .bind("questionGuid", questionGuid)
                .bindKotlin(FormInstanceQuestionEntity.Update(update))
                .execute()
            return@inTransaction when (updateCount) {
                0 -> throw FormInstanceQuestionNotFound()
                1 -> checkNotNull(get(formInstanceGuid, questionGuid))
                else -> badSql()
            }
        }
    }

    fun delete(formInstanceGuid: UUID, questionGuid: UUID) {
        jdbi.useTransaction<Exception> {
            val updateCount = it.createUpdate(sqlResource("delete"))
                .bind("formInstanceGuid", formInstanceGuid)
                .bind("questionGuid", questionGuid)
                .execute()
            return@useTransaction when (updateCount) {
                0 -> throw FormInstanceQuestionNotFound()
                1 -> Unit
                else -> badSql()
            }
        }
    }
}
