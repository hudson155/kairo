package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.sql.PolymorphicRowMapper
import com.piperframework.sql.bindNullForMissingArguments
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val FORM_INSTANCE_GUID_FOREIGN_KEY = "form_instance_question_form_instance_guid_fkey"
private const val QUESTION_GUID_FOREIGN_KEY = "form_instance_question_question_guid_fkey"

private class FormTemplateQuestionRowMapper : PolymorphicRowMapper<FormInstanceQuestionModel>("type") {
  override fun getClass(type: String) = when (FormTemplateQuestionModel.Type.valueOf(type)) {
    FormTemplateQuestionModel.Type.DATE -> FormInstanceDateQuestionModel::class.java
    FormTemplateQuestionModel.Type.RADIO_SELECTOR -> FormInstanceRadioSelectorQuestionModel::class.java
    FormTemplateQuestionModel.Type.TEXT -> FormInstanceTextQuestionModel::class.java
  }
}

@Singleton
internal class FormInstanceQuestionStore @Inject constructor(private val jdbi: Jdbi) : SqlStore(jdbi) {
  init {
    jdbi.registerRowMapper(FormTemplateQuestionRowMapper())
  }

  fun create(model: FormInstanceQuestionModel): FormInstanceQuestionModel {
    return jdbi.withHandle<FormInstanceQuestionModel, Exception> {
      try {
        it.createQuery(sqlResource("/store/formInstanceQuestion/create.sql"))
          .bindKotlin(model)
          .bindNullForMissingArguments()
          .mapTo(FormInstanceQuestionModel::class.java)
          .single()
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

  fun get(formInstanceGuid: UUID? = null, questionGuid: UUID? = null): List<FormInstanceQuestionModel> {
    return jdbi.withHandle<List<FormInstanceQuestionModel>, Exception> {
      it.createQuery(sqlResource("/store/formInstanceQuestion/get.sql")).build {
        if (formInstanceGuid != null) {
          conditions.add("form_instance_guid = :formInstanceGuid")
          bindings["formInstanceGuid"] = formInstanceGuid
        }
        if (questionGuid != null) {
          conditions.add("question_guid = :questionGuid")
          bindings["questionGuid"] = questionGuid
        }
      }
        .mapTo(FormInstanceQuestionModel::class.java)
        .list()
    }
  }

  fun update(
    formInstanceGuid: UUID,
    questionGuid: UUID,
    update: FormInstanceQuestionModel.Update,
  ): FormInstanceQuestionModel =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/formInstanceQuestion/update.sql"))
        .bind("formInstanceGuid", formInstanceGuid)
        .bind("questionGuid", questionGuid)
        .bindKotlin(update)
        .bindNullForMissingArguments()
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FormInstanceQuestionNotFound()
        1 -> get(formInstanceGuid = formInstanceGuid, questionGuid = questionGuid).single()
        else -> badSql()
      }
    }

  fun delete(formInstanceGuid: UUID, questionGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(sqlResource("/store/formInstanceQuestion/delete.sql"))
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
