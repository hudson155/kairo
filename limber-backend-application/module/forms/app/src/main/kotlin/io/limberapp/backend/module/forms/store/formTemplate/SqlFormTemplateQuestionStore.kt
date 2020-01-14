package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.exception.exception.badRequest.RankOutOfBounds
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionTable
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update
import java.util.UUID

internal class SqlFormTemplateQuestionStore @Inject constructor(
    database: Database
) : FormTemplateQuestionService, SqlStore(database) {

    override fun create(
        formTemplateId: UUID,
        models: List<FormTemplateQuestionModel>,
        rank: Int?
    ) = transaction<Unit> {
        val insertionRank = validateInsertionRank(formTemplateId, rank)
        incrementExistingRanks(formTemplateId, atLeast = insertionRank, incrementBy = models.size)
        FormTemplateQuestionTable.batchInsertIndexed(models) { i, model ->
            createFormTemplateQuestion(formTemplateId, model, insertionRank + i)
        }
    }

    override fun create(formTemplateId: UUID, model: FormTemplateQuestionModel, rank: Int?) = transaction<Unit> {
        val insertionRank = validateInsertionRank(formTemplateId, rank)
        incrementExistingRanks(formTemplateId, atLeast = insertionRank, incrementBy = 1)
        FormTemplateQuestionTable.insert { it.createFormTemplateQuestion(formTemplateId, model, insertionRank) }
    }

    private fun validateInsertionRank(formTemplateId: UUID, rank: Int?): Int {
        rank?.let { if (it < 0) throw RankOutOfBounds(it) }
        val maxExistingRank = FormTemplateQuestionTable.slice(
            FormTemplateQuestionTable.formTemplateGuid,
            FormTemplateQuestionTable.rank.max()
        ).select { FormTemplateQuestionTable.formTemplateGuid eq formTemplateId }
            .groupBy(FormTemplateQuestionTable.formTemplateGuid)
            .singleOrNull()?.get(FormTemplateQuestionTable.rank.max()) ?: -1
        rank?.let { if (it > maxExistingRank + 1) throw RankOutOfBounds(it) }
        return rank ?: maxExistingRank + 1
    }

    private fun incrementExistingRanks(formTemplateId: UUID, atLeast: Int, incrementBy: Int) {
        FormTemplateQuestionTable.update({
            (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                    (FormTemplateQuestionTable.rank greaterEq atLeast)
        }) {
            with(SqlExpressionBuilder) { it.update(rank, rank + incrementBy) }
        }
    }

    private fun InsertStatement<*>.createFormTemplateQuestion(
        formTemplateId: UUID,
        model: FormTemplateQuestionModel,
        index: Int
    ) {
        this[FormTemplateQuestionTable.createdDate] = model.created
        this[FormTemplateQuestionTable.guid] = model.id
        this[FormTemplateQuestionTable.formTemplateGuid] = formTemplateId
        this[FormTemplateQuestionTable.rank] = index
        this[FormTemplateQuestionTable.label] = model.label
        this[FormTemplateQuestionTable.helpText] = model.helpText
        this[FormTemplateQuestionTable.type] = model.type.name
        when (model) {
            is FormTemplateDateQuestionModel -> {
                this[FormTemplateQuestionTable.earliest] = model.earliest
                this[FormTemplateQuestionTable.latest] = model.latest
            }
            is FormTemplateTextQuestionModel -> {
                this[FormTemplateQuestionTable.multiLine] = model.multiLine
                this[FormTemplateQuestionTable.placeholder] = model.placeholder
                this[FormTemplateQuestionTable.validator] = model.validator?.pattern
            }
            else -> error("Unexpected question type: ${model::class.qualifiedName}")
        }
    }

    override fun get(formTemplateId: UUID, formTemplateQuestionId: UUID) = transaction {
        return@transaction FormTemplateQuestionTable.select {
            (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                    (FormTemplateQuestionTable.guid eq formTemplateQuestionId)
        }.singleOrNull()?.toFormTemplateQuestionModel()
    }

    override fun getByFormTemplateId(formTemplateId: UUID) = transaction {
        return@transaction FormTemplateQuestionTable.select {
            FormTemplateQuestionTable.formTemplateGuid eq formTemplateId
        }.orderBy(FormTemplateQuestionTable.rank).map { it.toFormTemplateQuestionModel() }
    }

    override fun update(
        formTemplateId: UUID,
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionModel.Update
    ) = TODO()

    override fun delete(formTemplateId: UUID, formTemplateQuestionId: UUID) = transaction<Unit> {
        FormTemplateQuestionTable.deleteAtMostOneWhere {
            (FormTemplateQuestionTable.formTemplateGuid eq formTemplateId) and
                    (FormTemplateQuestionTable.guid eq formTemplateQuestionId)
        }
            .ifEq(0) { throw FormTemplateQuestionNotFound() }
    }

    private fun ResultRow.toFormTemplateQuestionModel() =
        when (FormTemplateQuestionModel.Type.valueOf(this[FormTemplateQuestionTable.type])) {
            FormTemplateQuestionModel.Type.DATE -> FormTemplateDateQuestionModel(
                id = this[FormTemplateQuestionTable.guid],
                created = this[FormTemplateQuestionTable.createdDate],
                label = this[FormTemplateQuestionTable.label],
                helpText = this[FormTemplateQuestionTable.helpText],
                earliest = this[FormTemplateQuestionTable.earliest],
                latest = this[FormTemplateQuestionTable.latest]
            )
            FormTemplateQuestionModel.Type.TEXT -> FormTemplateTextQuestionModel(
                id = this[FormTemplateQuestionTable.guid],
                created = this[FormTemplateQuestionTable.createdDate],
                label = this[FormTemplateQuestionTable.label],
                helpText = this[FormTemplateQuestionTable.helpText],
                multiLine = checkNotNull(this[FormTemplateQuestionTable.multiLine]),
                placeholder = this[FormTemplateQuestionTable.placeholder],
                validator = this[FormTemplateQuestionTable.validator]?.let { Regex(it) }
            )
        }
}
