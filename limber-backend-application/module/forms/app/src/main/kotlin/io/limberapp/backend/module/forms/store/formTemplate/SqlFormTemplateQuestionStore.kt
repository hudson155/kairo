package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.exception.exception.badRequest.RankOutOfBounds
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.entity.formTemplate.FormTemplateQuestionTable
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateStatement
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
            .singleOrNull()
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

    override fun get(formTemplateId: UUID, formTemplateQuestionId: UUID) = transaction {
        val entity = FormTemplateQuestionTable
            .select {
                (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                        (FormTemplateQuestionTable.guid eq formTemplateQuestionId)
            }
            .singleOrNull() ?: return@transaction null
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
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionModel.Update
    ) = transaction {
        FormTemplateQuestionTable
            .updateExactlyOne(
                where = {
                    (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                            (FormTemplateQuestionTable.guid eq formTemplateQuestionId)
                },
                body = { it.updateFormTemplate(update) },
                notFound = { throw FormTemplateQuestionNotFound() }
            )
        return@transaction checkNotNull(get(formTemplateId, formTemplateQuestionId))
    }

    private fun UpdateStatement.updateFormTemplate(update: FormTemplateQuestionModel.Update) {
        update.label?.let { this[FormTemplateQuestionTable.label] = it }
        update.helpText?.let { this[FormTemplateQuestionTable.helpText] = it }
        when (update) {
            is FormTemplateDateQuestionModel.Update -> {
                update.earliest?.let { this[FormTemplateQuestionTable.earliest] = it }
                update.latest?.let { this[FormTemplateQuestionTable.latest] = it }
            }
            is FormTemplateTextQuestionModel.Update -> {
                update.multiLine?.let { this[FormTemplateQuestionTable.multiLine] = it }
                update.placeholder?.let { this[FormTemplateQuestionTable.placeholder] = it }
                update.validator?.let { this[FormTemplateQuestionTable.validator] = it.pattern }
            }
            else -> error("Unexpected question type: ${update::class.qualifiedName}")
        }
    }

    override fun delete(formTemplateId: UUID, formTemplateQuestionId: UUID) = transaction<Unit> {
        FormTemplateQuestionTable
            .deleteAtMostOneWhere {
                (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                        (FormTemplateQuestionTable.guid eq formTemplateQuestionId)
            }
            .ifEq(0) { throw FormTemplateQuestionNotFound() }
    }
}
