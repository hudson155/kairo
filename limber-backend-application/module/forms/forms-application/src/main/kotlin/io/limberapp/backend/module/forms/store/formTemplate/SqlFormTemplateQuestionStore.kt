package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.exception.exception.badRequest.RankOutOfBounds
import com.piperframework.store.SqlStore
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionTable
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.util.UUID

internal class SqlFormTemplateQuestionStore @Inject constructor(
    database: Database,
    private val sqlFormTemplateMapper: SqlFormTemplateMapper
) : FormTemplateQuestionStore, SqlStore(database) {
    override fun create(
        formTemplateId: UUID,
        models: List<FormTemplateQuestionModel>,
        rank: Int?
    ) = transaction<Unit> {
        val insertionRank = validateInsertionRank(formTemplateId, rank)
        incrementExistingRanks(formTemplateId, atLeast = insertionRank, incrementBy = models.size)
        FormTemplateQuestionTable
            .batchInsertIndexed(models) { i, model ->
                sqlFormTemplateMapper.formTemplateQuestionEntity(this, formTemplateId, model, insertionRank + i)
            }
    }

    override fun create(formTemplateId: UUID, model: FormTemplateQuestionModel, rank: Int?) = transaction<Unit> {
        val insertionRank = validateInsertionRank(formTemplateId, rank)
        incrementExistingRanks(formTemplateId, atLeast = insertionRank, incrementBy = 1)
        FormTemplateQuestionTable
            .insert { sqlFormTemplateMapper.formTemplateQuestionEntity(it, formTemplateId, model, insertionRank) }
    }

    private fun validateInsertionRank(formTemplateId: UUID, rank: Int?): Int {
        rank?.let { if (it < 0) throw RankOutOfBounds(it) }
        val resultRow = FormTemplateQuestionTable
            .slice(
                FormTemplateQuestionTable.formTemplateGuid,
                FormTemplateQuestionTable.rank.max()
            )
            .select { FormTemplateQuestionTable.formTemplateGuid eq formTemplateId }
            .groupBy(FormTemplateQuestionTable.formTemplateGuid)
            .singleNullOrThrow()
        val maxExistingRank = resultRow?.get(FormTemplateQuestionTable.rank.max()) ?: -1
        rank?.let { if (it > maxExistingRank + 1) throw RankOutOfBounds(it) }
        return rank ?: maxExistingRank + 1
    }

    private fun incrementExistingRanks(formTemplateId: UUID, atLeast: Int, incrementBy: Int) {
        FormTemplateQuestionTable
            .update(
                where = {
                    (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                            (FormTemplateQuestionTable.rank greaterEq atLeast)
                },
                body = { with(SqlExpressionBuilder) { it.update(rank, rank + incrementBy) } }
            )
    }

    override fun get(formTemplateId: UUID, questionId: UUID) = transaction {
        val entity = FormTemplateQuestionTable
            .select {
                (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                        (FormTemplateQuestionTable.guid eq questionId)
            }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlFormTemplateMapper.formTemplateQuestionModel(entity)
    }

    override fun getByFormTemplateId(formTemplateId: UUID) = transaction {
        return@transaction FormTemplateQuestionTable
            .select { FormTemplateQuestionTable.formTemplateGuid eq formTemplateId }
            .orderBy(FormTemplateQuestionTable.rank)
            .map { sqlFormTemplateMapper.formTemplateQuestionModel(it) }
    }

    override fun update(
        formTemplateId: UUID,
        questionId: UUID,
        update: FormTemplateQuestionModel.Update
    ) = transaction {
        FormTemplateQuestionTable
            .updateExactlyOne(
                where = {
                    (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                            (FormTemplateQuestionTable.guid eq questionId)
                },
                body = { sqlFormTemplateMapper.formTemplateQuestionEntity(it, update) },
                notFound = { throw FormTemplateQuestionNotFound() }
            )
        return@transaction checkNotNull(get(formTemplateId, questionId))
    }

    override fun delete(formTemplateId: UUID, questionId: UUID) = transaction<Unit> {
        FormTemplateQuestionTable
            .deleteExactlyOne(
                where = {
                    (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                            (FormTemplateQuestionTable.guid eq questionId)
                },
                notFound = { throw FormTemplateQuestionNotFound() }
            )
    }
}
