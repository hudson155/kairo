package io.limberapp.backend.module.forms.store.formInstance

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.finder.Finder
import com.piperframework.sql.PolymorphicRowMapper
import com.piperframework.sql.bindNullForMissingArguments
import com.piperframework.store.SqlStore
import com.piperframework.store.isForeignKeyViolation
import com.piperframework.store.withFinder
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceQuestionFinder
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val FK_FORM_INSTANCE_GUID = "fk__form_instance_question__form_instance_guid"
private const val FK_QUESTION_GUID = "fk__form_instance_question__question_guid"

private class FormTemplateQuestionRowMapper : PolymorphicRowMapper<FormInstanceQuestionModel>("type") {
  override fun getClass(type: String) = when (FormTemplateQuestionModel.Type.valueOf(type)) {
    FormTemplateQuestionModel.Type.DATE -> FormInstanceDateQuestionModel::class.java
    FormTemplateQuestionModel.Type.RADIO_SELECTOR -> FormInstanceRadioSelectorQuestionModel::class.java
    FormTemplateQuestionModel.Type.TEXT -> FormInstanceTextQuestionModel::class.java
  }
}

@Singleton
internal class FormInstanceQuestionStore @Inject constructor(
  private val jdbi: Jdbi,
) : SqlStore(jdbi), Finder<FormInstanceQuestionModel, FormInstanceQuestionFinder> {
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
      error.isForeignKeyViolation(FK_FORM_INSTANCE_GUID) -> throw FormInstanceNotFound()
      error.isForeignKeyViolation(FK_QUESTION_GUID) -> throw FormTemplateQuestionNotFound()
      else -> throw e
    }
  }

  override fun <R> find(
    result: (Iterable<FormInstanceQuestionModel>) -> R,
    query: FormInstanceQuestionFinder.() -> Unit,
  ) = withHandle { handle ->
    handle.createQuery(sqlResource("/store/formInstanceQuestion/find.sql"))
      .withFinder(FormInstanceQuestionQueryBuilder().apply(query))
      .mapTo(FormInstanceQuestionModel::class.java)
      .let(result)
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
        1 -> findOnlyOrThrow { questionGuid(questionGuid) }
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
