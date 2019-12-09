package io.limberapp.backend.module.forms.mapper.api.formTemplate

import com.google.inject.Inject
import com.piperframework.util.uuid.unknown
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.orgs.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.orgs.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import kotlin.reflect.KClass

private val TEXT_QUESTION_MAX_LENGTH_MULTILINE = 10_000
private val TEXT_QUESTION_MAX_LENGTH_ONE_LINE = 200

internal class FormTemplateQuestionMapper @Inject constructor(
    private val uuidGenerator: UuidGenerator
) {

    fun defaultModels() = listOf(
        FormTemplateTextQuestionModel(
            id = uuidGenerator.generate(),
            label = "Worker Name",
            helpText = null,
            width = FormTemplateQuestionModel.Width.HALF_WIDTH,
            multiLine = false,
            placeholder = null,
            validator = null
        ),
        FormTemplateDateQuestionModel(
            id = uuidGenerator.generate(),
            label = "Date",
            helpText = null,
            width = FormTemplateQuestionModel.Width.HALF_WIDTH,
            earliest = null,
            latest = null
        ),
        FormTemplateTextQuestionModel(
            id = uuidGenerator.generate(),
            label = "Description",
            helpText = null,
            width = FormTemplateQuestionModel.Width.FULL_WIDTH,
            multiLine = true,
            placeholder = null,
            validator = null
        )
    )

    fun model(rep: FormTemplateQuestionRep.Creation) = when (rep) {
        is FormTemplateDateQuestionRep.Creation -> FormTemplateDateQuestionModel(
            id = uuidGenerator.generate(),
            label = rep.label,
            helpText = rep.helpText,
            width = rep.width,
            earliest = rep.earliest,
            latest = rep.latest
        )
        is FormTemplateTextQuestionRep.Creation -> FormTemplateTextQuestionModel(
            id = uuidGenerator.generate(),
            label = rep.label,
            helpText = rep.helpText,
            width = rep.width,
            multiLine = rep.multiLine,
            placeholder = rep.placeholder,
            validator = rep.validator
        )
        else -> unknown(rep::class)
    }

    fun completeRep(model: FormTemplateQuestionModel) = when (model) {
        is FormTemplateDateQuestionModel -> FormTemplateDateQuestionRep.Complete(
            id = model.id,
            label = model.label,
            helpText = model.helpText,
            width = model.width,
            earliest = model.earliest,
            latest = model.latest
        )
        is FormTemplateTextQuestionModel -> FormTemplateTextQuestionRep.Complete(
            id = uuidGenerator.generate(),
            label = model.label,
            helpText = model.helpText,
            width = model.width,
            maxLength = if (model.multiLine) TEXT_QUESTION_MAX_LENGTH_MULTILINE else TEXT_QUESTION_MAX_LENGTH_ONE_LINE,
            multiLine = model.multiLine,
            placeholder = model.placeholder,
            validator = model.validator
        )
        else -> unknown(model::class)
    }

    fun update(rep: FormTemplateQuestionRep.Update) = when (rep) {
        is FormTemplateDateQuestionRep.Update -> FormTemplateDateQuestionModel.Update(
            label = rep.label,
            helpText = rep.helpText,
            width = rep.width,
            earliest = rep.earliest,
            latest = rep.latest
        )
        is FormTemplateTextQuestionRep.Update -> FormTemplateTextQuestionModel.Update(
            label = rep.label,
            helpText = rep.helpText,
            width = rep.width,
            multiLine = rep.multiLine,
            placeholder = rep.placeholder,
            validator = rep.validator
        )
        else -> unknown(rep::class)
    }

    private fun unknown(clazz: KClass<*>): Nothing = unknown("form template question", clazz)
}
