package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.google.inject.Singleton
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionFinder
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateYesNoQuestionModel
import io.limberapp.common.finder.Finder
import io.limberapp.common.sql.PolymorphicRowMapper
import io.limberapp.common.sql.bindNullForMissingArguments
import io.limberapp.common.store.SqlStore
import io.limberapp.common.store.isForeignKeyViolation
import io.limberapp.common.store.isNotNullConstraintViolation
import io.limberapp.common.store.withFinder
import io.limberapp.exception.badRequest.RankOutOfBounds
import io.limberapp.exception.unprocessableEntity.unprocessable
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.util.*

private const val COL_FORM_TEMPLATE_GUID = "form_template_guid"
private const val FK_FORM_TEMPLATE_GUID = "fk__form_template__form_template_guid"

private class FormTemplateQuestionRowMapper : PolymorphicRowMapper<FormTemplateQuestionModel>("type") {
  override fun getClass(type: String) = when (FormTemplateQuestionModel.Type.valueOf(type)) {
    FormTemplateQuestionModel.Type.DATE -> FormTemplateDateQuestionModel::class.java
    FormTemplateQuestionModel.Type.RADIO_SELECTOR -> FormTemplateRadioSelectorQuestionModel::class.java
    FormTemplateQuestionModel.Type.TEXT -> FormTemplateTextQuestionModel::class.java
    FormTemplateQuestionModel.Type.YES_NO -> FormTemplateYesNoQuestionModel::class.java
  }
}

@Singleton
internal class FormTemplateQuestionStore @Inject constructor(
    jdbi: Jdbi,
) : SqlStore(jdbi), Finder<FormTemplateQuestionModel, FormTemplateQuestionFinder> {
  init {
    jdbi.registerRowMapper(FormTemplateQuestionRowMapper())
  }

  fun create(featureGuid: UUID, model: FormTemplateQuestionModel, rank: Int? = null): FormTemplateQuestionModel =
      inTransaction { handle ->
        val insertionRank = validateInsertionRank(model.formTemplateGuid, rank)
        incrementExistingRanks(model.formTemplateGuid, atLeast = insertionRank)
        try {
          handle.createQuery(sqlResource("/store/formTemplateQuestion/create.sql"))
              .bind("featureGuid", featureGuid)
              .bindKotlin(model)
              .bind("rank", insertionRank)
              .bindNullForMissingArguments()
              .mapTo(FormTemplateQuestionModel::class.java)
              .single()
        } catch (e: UnableToExecuteStatementException) {
          handleCreateError(e)
        }
      }

  private fun validateInsertionRank(formTemplateGuid: UUID, rank: Int?): Int {
    rank?.let { if (it < 0) throw RankOutOfBounds(it) }
    val maxExistingRank = withHandle { handle ->
      handle.createQuery(sqlResource("/store/formTemplateQuestion/getMaxExistingRankByFormTemplateGuid.sql"))
          .bind("formTemplateGuid", formTemplateGuid)
          .asInt()
    } ?: -1
    rank?.let { if (it > maxExistingRank + 1) throw RankOutOfBounds(it) }
    return rank ?: maxExistingRank + 1
  }

  private fun incrementExistingRanks(formTemplateGuid: UUID, atLeast: Int) {
    withHandle { handle ->
      handle.createUpdate(sqlResource("/store/formTemplateQuestion/incrementExistingRanksByFormTemplateGuid.sql"))
          .bind("formTemplateGuid", formTemplateGuid)
          .bind("atLeast", atLeast)
          .execute()
    }
  }

  override fun <R> find(
      result: (Iterable<FormTemplateQuestionModel>) -> R,
      query: FormTemplateQuestionFinder.() -> Unit,
  ) =
      withHandle { handle ->
        handle.createQuery(sqlResource("/store/formTemplateQuestion/find.sql"))
            .withFinder(FormTemplateQuestionQueryBuilder().apply(query))
            .mapTo(FormTemplateQuestionModel::class.java)
            .let(result)
      }

  fun update(
      featureGuid: UUID,
      formTemplateGuid: UUID,
      questionGuid: UUID,
      update: FormTemplateQuestionModel.Update,
  ): FormTemplateQuestionModel =
      inTransaction { handle ->
        handle.createQuery(sqlResource("/store/formTemplateQuestion/update.sql"))
            .bind("featureGuid", featureGuid)
            .bind("formTemplateGuid", formTemplateGuid)
            .bind("questionGuid", questionGuid)
            .bindKotlin(update)
            .bindNullForMissingArguments()
            .mapTo(FormTemplateQuestionModel::class.java)
            .singleNullOrThrow() ?: throw FormTemplateQuestionNotFound()
      }

  fun delete(
      featureGuid: UUID,
      formTemplateGuid: UUID,
      questionGuid: UUID,
  ): Unit =
      inTransaction { handle ->
        handle.createUpdate(sqlResource("/store/formTemplateQuestion/delete.sql"))
            .bind("featureGuid", featureGuid)
            .bind("formTemplateGuid", formTemplateGuid)
            .bind("questionGuid", questionGuid)
            .updateOnly() ?: throw FormTemplateQuestionNotFound()
      }

  private fun handleCreateError(e: UnableToExecuteStatementException): Nothing {
    val error = e.serverErrorMessage ?: throw e
    when {
      error.isForeignKeyViolation(FK_FORM_TEMPLATE_GUID) -> throw FormTemplateNotFound().unprocessable()
      error.isNotNullConstraintViolation(COL_FORM_TEMPLATE_GUID) -> throw FormTemplateNotFound().unprocessable()
      else -> throw e
    }
  }
}
