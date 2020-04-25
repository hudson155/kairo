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
        formTemplateGuid: UUID,
        models: List<FormTemplateQuestionModel>,
        rank: Int?
    ) = transaction<Unit> {
        val insertionRank = validateInsertionRank(formTemplateGuid, rank)
        incrementExistingRanks(formTemplateGuid, atLeast = insertionRank, incrementBy = models.size)
        FormTemplateQuestionTable
            .batchInsertIndexed(models) { i, model ->
                sqlFormTemplateMapper.formTemplateQuestionEntity(this, formTemplateGuid, model, insertionRank + i)
            }
    }

    override fun create(formTemplateGuid: UUID, model: FormTemplateQuestionModel, rank: Int?) = transaction<Unit> {
        val insertionRank = validateInsertionRank(formTemplateGuid, rank)
        incrementExistingRanks(formTemplateGuid, atLeast = insertionRank, incrementBy = 1)
        FormTemplateQuestionTable
            .insert { sqlFormTemplateMapper.formTemplateQuestionEntity(it, formTemplateGuid, model, insertionRank) }
    }

    private fun validateInsertionRank(formTemplateGuid: UUID, rank: Int?): Int {
        rank?.let { if (it < 0) throw RankOutOfBounds(it) }
        val resultRow = FormTemplateQuestionTable
            .slice(
                FormTemplateQuestionTable.formTemplateGuid,
                FormTemplateQuestionTable.rank.max()
            )
            .select { FormTemplateQuestionTable.formTemplateGuid eq formTemplateGuid }
            .groupBy(FormTemplateQuestionTable.formTemplateGuid)
            .singleNullOrThrow()
        val maxExistingRank = resultRow?.get(FormTemplateQuestionTable.rank.max()) ?: -1
        rank?.let { if (it > maxExistingRank + 1) throw RankOutOfBounds(it) }
        return rank ?: maxExistingRank + 1
    }

    private fun incrementExistingRanks(formTemplateGuid: UUID, atLeast: Int, incrementBy: Int) {
        FormTemplateQuestionTable
            .update(
                where = {
                    (FormTemplateQuestionTable.formTemplateGuid eq formTemplateGuid) and
                            (FormTemplateQuestionTable.rank greaterEq atLeast)
                },
                body = { with(SqlExpressionBuilder) { it.update(rank, rank + incrementBy) } }
            )
    }

    override fun get(formTemplateGuid: UUID, questionGuid: UUID) = transaction {
        val entity = FormTemplateQuestionTable
            .select {
                (FormTemplateQuestionTable.formTemplateGuid eq formTemplateGuid) and
                        (FormTemplateQuestionTable.guid eq questionGuid)
            }
            .singleNullOrThrow() ?: return@transaction null
        return@transaction sqlFormTemplateMapper.formTemplateQuestionModel(entity)
    }

    override fun getByFormTemplateGuid(formTemplateGuid: UUID) = transaction {
        return@transaction FormTemplateQuestionTable
            .select { FormTemplateQuestionTable.formTemplateGuid eq formTemplateGuid }
            .orderBy(FormTemplateQuestionTable.rank)
            .map { sqlFormTemplateMapper.formTemplateQuestionModel(it) }
    }

    override fun update(
        formTemplateGuid: UUID,
        questionGuid: UUID,
        update: FormTemplateQuestionModel.Update
    ) = transaction {
        FormTemplateQuestionTable
            .updateExactlyOne(
                where = {
                    (FormTemplateQuestionTable.formTemplateGuid eq formTemplateGuid) and
                            (FormTemplateQuestionTable.guid eq questionGuid)
                },
                body = { sqlFormTemplateMapper.formTemplateQuestionEntity(it, update) },
                notFound = { throw FormTemplateQuestionNotFound() }
            )
        return@transaction checkNotNull(get(formTemplateGuid, questionGuid))
    }

    override fun delete(formTemplateGuid: UUID, questionGuid: UUID) = transaction<Unit> {
        FormTemplateQuestionTable
            .deleteExactlyOne(
                where = {
                    (FormTemplateQuestionTable.formTemplateGuid eq formTemplateGuid) and
                            (FormTemplateQuestionTable.guid eq questionGuid)
                },
                notFound = { throw FormTemplateQuestionNotFound() }
            )
    }
}
