package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.google.inject.Singleton
import com.piperframework.exception.exception.badRequest.RankOutOfBounds
import com.piperframework.finder.Finder
import com.piperframework.sql.PolymorphicRowMapper
import com.piperframework.sql.bindNullForMissingArguments
import com.piperframework.store.SqlStore
import com.piperframework.store.withFinder
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateQuestionFinder
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

private class FormTemplateQuestionRowMapper : PolymorphicRowMapper<FormTemplateQuestionModel>("type") {
  override fun getClass(type: String) = when (FormTemplateQuestionModel.Type.valueOf(type)) {
    FormTemplateQuestionModel.Type.DATE -> FormTemplateDateQuestionModel::class.java
    FormTemplateQuestionModel.Type.RADIO_SELECTOR -> FormTemplateRadioSelectorQuestionModel::class.java
    FormTemplateQuestionModel.Type.TEXT -> FormTemplateTextQuestionModel::class.java
  }
}

@Singleton
internal class FormTemplateQuestionStore @Inject constructor(
  private val jdbi: Jdbi,
) : SqlStore(jdbi), Finder<FormTemplateQuestionModel, FormTemplateQuestionFinder> {
  init {
    jdbi.registerRowMapper(FormTemplateQuestionRowMapper())
  }

  fun create(models: List<FormTemplateQuestionModel>, rank: Int? = null) {
    // Batch creation is only supported for questions in the same form template.
    val formTemplateGuid = models.map { it.formTemplateGuid }.distinct().singleOrNull() ?: return
    jdbi.useTransaction<Exception> {
      val insertionRank = validateInsertionRank(formTemplateGuid, rank)
      incrementExistingRanks(formTemplateGuid, atLeast = insertionRank, incrementBy = models.size)
      it.prepareBatch(sqlResource("/store/formTemplateQuestion/create.sql")).apply {
        models.forEachIndexed { i, model ->
          this
            .bind("rank", insertionRank + i)
            .bindKotlin(model)
            .bindNullForMissingArguments()
            .add()
        }
      }.execute()
    }
  }

  fun create(model: FormTemplateQuestionModel, rank: Int? = null): FormTemplateQuestionModel {
    return jdbi.inTransaction<FormTemplateQuestionModel, Exception> {
      val insertionRank = validateInsertionRank(model.formTemplateGuid, rank)
      incrementExistingRanks(model.formTemplateGuid, atLeast = insertionRank, incrementBy = 1)
      it.createQuery(sqlResource("/store/formTemplateQuestion/create.sql"))
        .bind("rank", insertionRank)
        .bindKotlin(model)
        .bindNullForMissingArguments()
        .mapTo(FormTemplateQuestionModel::class.java)
        .single()
    }
  }

  private fun validateInsertionRank(formTemplateGuid: UUID, rank: Int?): Int {
    rank?.let { if (it < 0) throw RankOutOfBounds(it) }
    val maxExistingRank = jdbi.withHandle<Int, Exception> {
      it.createQuery(sqlResource("/store/formTemplateQuestion/getMaxExistingRankByFormTemplateGuid.sql"))
        .bind("formTemplateGuid", formTemplateGuid)
        .asInt()
    } ?: -1
    rank?.let { if (it > maxExistingRank + 1) throw RankOutOfBounds(it) }
    return rank ?: maxExistingRank + 1
  }

  private fun incrementExistingRanks(formTemplateGuid: UUID, atLeast: Int, incrementBy: Int) {
    jdbi.useHandle<Exception> {
      it.createUpdate(sqlResource("/store/formTemplateQuestion/incrementExistingRanksByFormTemplateGuid.sql"))
        .bind("formTemplateGuid", formTemplateGuid)
        .bind("atLeast", atLeast)
        .bind("incrementBy", incrementBy)
        .execute()
    }
  }

  override fun <R> find(
    result: (Iterable<FormTemplateQuestionModel>) -> R,
    query: FormTemplateQuestionFinder.() -> Unit,
  ) = withHandle { handle ->
    handle.createQuery(sqlResource("/store/formTemplateQuestion/find.sql"))
      .withFinder(FormTemplateQuestionQueryBuilder().apply(query))
      .mapTo(FormTemplateQuestionModel::class.java)
      .let(result)
  }

  fun update(questionGuid: UUID, update: FormTemplateQuestionModel.Update): FormTemplateQuestionModel =
    inTransaction { handle ->
      val updateCount = handle.createUpdate(sqlResource("/store/formTemplateQuestion/update.sql"))
        .bind("questionGuid", questionGuid)
        .bindKotlin(update)
        .bindNullForMissingArguments()
        .execute()
      return@inTransaction when (updateCount) {
        0 -> throw FormTemplateQuestionNotFound()
        1 -> findOnlyOrThrow { questionGuid(questionGuid) }
        else -> badSql()
      }
    }

  fun delete(questionGuid: UUID) {
    jdbi.useTransaction<Exception> {
      val updateCount = it.createUpdate(sqlResource("/store/formTemplateQuestion/delete.sql"))
        .bind("questionGuid", questionGuid)
        .execute()
      return@useTransaction when (updateCount) {
        0 -> throw FormTemplateQuestionNotFound()
        1 -> Unit
        else -> badSql()
      }
    }
  }
}
