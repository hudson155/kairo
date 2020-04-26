package io.limberapp.backend.module.forms.entity.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

internal data class FormInstanceQuestionEntity(
    val createdDate: LocalDateTime,
    val questionGuid: UUID?,
    val type: FormTemplateQuestionModel.Type,
    val text: String?,
    val date: LocalDate?,
    val selections: Set<String>?
) {
    data class Update(
        val text: String?,
        val date: LocalDate?,
        val selections: Set<String>?
    ) {
        constructor(update: FormInstanceQuestionModel.Update) : this(
            text = (update as? FormInstanceTextQuestionModel.Update)?.text,
            date = (update as? FormInstanceDateQuestionModel.Update)?.date,
            selections = (update as? FormInstanceRadioSelectorQuestionModel.Update)?.selection?.let { setOf(it) }
        )

        constructor(model: FormInstanceQuestionModel) : this(
            text = (model as? FormInstanceTextQuestionModel)?.text,
            date = (model as? FormInstanceDateQuestionModel)?.date,
            selections = (model as? FormInstanceRadioSelectorQuestionModel)?.selection?.let { setOf(it) }
        )
    }

    constructor(model: FormInstanceQuestionModel) : this(
        createdDate = model.createdDate,
        questionGuid = model.questionGuid,
        type = model.type,
        text = (model as? FormInstanceTextQuestionModel)?.text,
        date = (model as? FormInstanceDateQuestionModel)?.date,
        selections = (model as? FormInstanceRadioSelectorQuestionModel)?.let { setOf(it.selection) }
    )

    fun asModel() = when (type) {
        FormTemplateQuestionModel.Type.DATE -> FormInstanceDateQuestionModel(
            createdDate = createdDate,
            questionGuid = questionGuid,
            date = checkNotNull(date)
        )
        FormTemplateQuestionModel.Type.RADIO_SELECTOR -> FormInstanceRadioSelectorQuestionModel(
            createdDate = createdDate,
            questionGuid = questionGuid,
            selection = checkNotNull(selections).single()
        )
        FormTemplateQuestionModel.Type.TEXT -> FormInstanceTextQuestionModel(
            createdDate = createdDate,
            questionGuid = questionGuid,
            text = checkNotNull(text)
        )
    }
}
