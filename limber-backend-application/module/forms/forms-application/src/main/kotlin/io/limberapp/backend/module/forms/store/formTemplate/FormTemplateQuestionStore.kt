package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.exception.exception.badRequest.RankOutOfBounds
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionEntity
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.UUID

internal class FormTemplateQuestionStore @Inject constructor(private val jdbi: Jdbi) : SqlStore() {
    fun create(models: List<FormTemplateQuestionModel>, rank: Int? = null) {
        // Batch creation is only supported for questions in the same form template.
        val formTemplateGuid = models.map { it.formTemplateGuid }.distinct().singleOrNull() ?: return
        jdbi.useTransaction<Exception> {
            val insertionRank = validateInsertionRank(formTemplateGuid, rank)
            incrementExistingRanks(formTemplateGuid, atLeast = insertionRank, incrementBy = models.size)
            it.prepareBatch(sqlResource("create")).apply {
                models.forEachIndexed { i, model ->
                    this
                        .bind("rank", insertionRank + i)
                        .bindKotlin(FormTemplateQuestionEntity(model))
                        .add()
                }
            }.execute()
        }
    }

    fun create(model: FormTemplateQuestionModel, rank: Int? = null) {
        jdbi.useTransaction<Exception> {
            val insertionRank = validateInsertionRank(model.formTemplateGuid, rank)
            incrementExistingRanks(model.formTemplateGuid, atLeast = insertionRank, incrementBy = 1)
            it.createUpdate(sqlResource("create"))
                .bind("rank", insertionRank)
                .bindKotlin(FormTemplateQuestionEntity(model))
                .execute()
        }
    }

    private fun validateInsertionRank(formTemplateGuid: UUID, rank: Int?): Int {
        rank?.let { if (it < 0) throw RankOutOfBounds(it) }
        val maxExistingRank = jdbi.withHandle<Int, Exception> {
            it.createQuery(
                    """
                    SELECT MAX(rank)
                    FROM forms.form_template_question
                    WHERE form_template_guid = :formTemplateGuid
                    """.trimIndent()
                )
                .bind("formTemplateGuid", formTemplateGuid)
                .asInt()
        } ?: -1
        rank?.let { if (it > maxExistingRank + 1) throw RankOutOfBounds(it) }
        return rank ?: maxExistingRank + 1
    }

    private fun incrementExistingRanks(formTemplateGuid: UUID, atLeast: Int, incrementBy: Int) {
        jdbi.useHandle<Exception> {
            it.createUpdate(
                    """
                    UPDATE forms.form_template_question
                    SET rank = rank + :incrementBy
                    WHERE form_template_guid = :formTemplateGuid
                      AND rank >= :atLeast
                    """.trimIndent()
                )
                .bind("formTemplateGuid", formTemplateGuid)
                .bind("atLeast", atLeast)
                .bind("incrementBy", incrementBy)
                .execute()
        }
    }

    fun get(questionGuid: UUID): FormTemplateQuestionModel? {
        return jdbi.withHandle<FormTemplateQuestionModel?, Exception> {
            it.createQuery(
                    """
                    SELECT *
                    FROM forms.form_template_question
                    WHERE guid = :guid
                    """.trimIndent()
                )
                .bind("guid", questionGuid)
                .mapTo(FormTemplateQuestionEntity::class.java)
                .singleNullOrThrow()
                ?.asModel()
        }
    }

    fun getByFormTemplateGuid(formTemplateGuid: UUID): List<FormTemplateQuestionModel> {
        return jdbi.withHandle<List<FormTemplateQuestionModel>, Exception> {
            it.createQuery(
                    """
                    SELECT *
                    FROM forms.form_template_question
                    WHERE form_template_guid = :formTemplateGuid
                    ORDER BY rank
                    """.trimIndent()
                )
                .bind("formTemplateGuid", formTemplateGuid)
                .mapTo(FormTemplateQuestionEntity::class.java)
                .map { it.asModel() }
                .toList()
        }
    }

    fun update(questionGuid: UUID, update: FormTemplateQuestionModel.Update): FormTemplateQuestionModel {
        return jdbi.inTransaction<FormTemplateQuestionModel, Exception> {
            val updateCount = it.createUpdate(sqlResource("update"))
                .bind("questionGuid", questionGuid)
                .bindKotlin(FormTemplateQuestionEntity.Update(update))
                .execute()
            return@inTransaction when (updateCount) {
                0 -> throw FormTemplateQuestionNotFound()
                1 -> checkNotNull(get(questionGuid))
                else -> badSql()
            }
        }
    }

    fun delete(questionGuid: UUID) {
        jdbi.useTransaction<Exception> {
            val updateCount = it.createUpdate(
                    """
                    DELETE
                    FROM forms.form_template_question
                    WHERE guid = :questionGuid
                    """.trimIndent()
                )
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
