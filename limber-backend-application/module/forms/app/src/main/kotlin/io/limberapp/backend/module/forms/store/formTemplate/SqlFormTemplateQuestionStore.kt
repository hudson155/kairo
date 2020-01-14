package io.limberapp.backend.module.forms.store.formTemplate

import com.google.inject.Inject
import com.piperframework.store.SqlStore
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionTable
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

internal class SqlFormTemplateQuestionStore @Inject constructor(
    database: Database
) : FormTemplateQuestionService, SqlStore(database) {

    override fun create(
        formTemplateId: UUID,
        models: List<FormTemplateQuestionModel>,
        index: Int?
    ) = transaction<Unit> {
        FormTemplateQuestionTable.batchInsert(models) { model -> createFormTemplateQuestion(formTemplateId, model) }
    }

    override fun create(formTemplateId: UUID, model: FormTemplateQuestionModel, index: Int?) = transaction<Unit> {
        FormTemplateQuestionTable.insert { it.createFormTemplateQuestion(formTemplateId, model) }
    }

    private fun InsertStatement<*>.createFormTemplateQuestion(formTemplateId: UUID, model: FormTemplateQuestionModel) {
        this[FormTemplateQuestionTable.createdDate] = model.created
        this[FormTemplateQuestionTable.guid] = model.id
        this[FormTemplateQuestionTable.formTemplateGuid] = formTemplateId
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
        }.map { it.toFormTemplateQuestionModel() }
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
