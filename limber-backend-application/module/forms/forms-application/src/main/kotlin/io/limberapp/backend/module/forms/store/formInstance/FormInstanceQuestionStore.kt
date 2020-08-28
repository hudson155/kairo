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
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionFinder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
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
  jdbi: Jdbi,
) : SqlStore(jdbi), Finder<FormInstanceQuestionModel, FormInstanceQuestionFinder> {
  init {
    jdbi.registerRowMapper(FormTemplateQuestionRowMapper())
  }

  fun upsert(featureGuid: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel =
    withHandle { handle ->
      return@withHandle try {
        handle.createQuery(sqlResource("/store/formInstanceQuestion/upsert.sql"))
          .bind("featureGuid", featureGuid)
          .bindKotlin(model)
          .bindNullForMissingArguments()
          .mapTo(FormInstanceQuestionModel::class.java)
          .singleNullOrThrow()
      } catch (e: UnableToExecuteStatementException) {
        handleCreateError(e)
      } ?: throw FormInstanceQuestionNotFound()
    }

  override fun <R> find(
    result: (Iterable<FormInstanceQuestionModel>) -> R,
    query: FormInstanceQuestionFinder.() -> Unit,
  ) =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/formInstanceQuestion/find.sql"))
        .withFinder(FormInstanceQuestionQueryBuilder().apply(query))
        .mapTo(FormInstanceQuestionModel::class.java)
        .let(result)
    }

  fun delete(featureGuid: UUID, formInstanceGuid: UUID, questionGuid: UUID): Unit =
    inTransaction { handle ->
      return@inTransaction handle.createUpdate(sqlResource("/store/formInstanceQuestion/delete.sql"))
        .bind("featureGuid", featureGuid)
        .bind("formInstanceGuid", formInstanceGuid)
        .bind("questionGuid", questionGuid)
        .updateOnly() ?: throw FormInstanceQuestionNotFound()
    }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isForeignKeyViolation(FK_FORM_INSTANCE_GUID) -> throw FormInstanceNotFound()
      error.isForeignKeyViolation(FK_QUESTION_GUID) -> throw FormTemplateQuestionNotFound()
      else -> throw e
    }
  }
}
