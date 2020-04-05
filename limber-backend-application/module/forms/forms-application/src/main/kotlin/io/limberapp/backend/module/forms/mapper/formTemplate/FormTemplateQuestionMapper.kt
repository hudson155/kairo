package io.limberapp.backend.module.forms.mapper.formTemplate

import com.google.inject.Inject
import com.piperframework.util.unknownType
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import java.time.Clock
import java.time.LocalDateTime
import kotlin.reflect.KClass

private const val TEXT_QUESTION_MAX_LENGTH_MULTILINE = 10_000
private const val TEXT_QUESTION_MAX_LENGTH_ONE_LINE = 200

internal class FormTemplateQuestionMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) {

    fun defaultModels() = listOf(
        FormTemplateTextQuestionModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            label = "Worker name",
            helpText = null,
            multiLine = false,
            placeholder = null,
            validator = null
        ),
        FormTemplateDateQuestionModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            label = "Date",
            helpText = null,
            earliest = null,
            latest = null
        ),
        FormTemplateTextQuestionModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            label = "Description",
            helpText = null,
            multiLine = true,
            placeholder = null,
            validator = null
        ),
        FormTemplateRadioSelectorQuestionModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            label = "Description",
            helpText = null,
            options = mutableSetOf("test_option_one", "test_option_two")
        )
    )

    fun model(rep: FormTemplateQuestionRep.Creation) = when (rep) {
        is FormTemplateDateQuestionRep.Creation -> FormTemplateDateQuestionModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            label = rep.label,
            helpText = rep.helpText,
            earliest = rep.earliest,
            latest = rep.latest
        )
        is FormTemplateTextQuestionRep.Creation -> FormTemplateTextQuestionModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            label = rep.label,
            helpText = rep.helpText,
            multiLine = rep.multiLine,
            placeholder = rep.placeholder,
            validator = rep.validator
        )
        is FormTemplateRadioSelectorQuestionRep.Creation -> FormTemplateRadioSelectorQuestionModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            label = rep.label,
            helpText = rep.helpText,
            options = rep.options
        )
        else -> unknownFormTemplateQuestion(rep::class)
    }

    fun completeRep(model: FormTemplateQuestionModel) = when (model) {
        is FormTemplateDateQuestionModel -> FormTemplateDateQuestionRep.Complete(
            id = model.id,
            created = model.created,
            label = model.label,
            helpText = model.helpText,
            earliest = model.earliest,
            latest = model.latest
        )
        is FormTemplateTextQuestionModel -> FormTemplateTextQuestionRep.Complete(
            id = model.id,
            created = model.created,
            label = model.label,
            helpText = model.helpText,
            maxLength = if (model.multiLine) TEXT_QUESTION_MAX_LENGTH_MULTILINE else TEXT_QUESTION_MAX_LENGTH_ONE_LINE,
            multiLine = model.multiLine,
            placeholder = model.placeholder,
            validator = model.validator
        )
        is FormTemplateRadioSelectorQuestionModel -> FormTemplateRadioSelectorQuestionRep.Complete (
            id = model.id,
            created = model.created,
            label = model.label,
            helpText = model.helpText,
            options = model.options
        )
        else -> unknownFormTemplateQuestion(model::class)
    }

    fun update(rep: FormTemplateQuestionRep.Update) = when (rep) {
        is FormTemplateDateQuestionRep.Update -> FormTemplateDateQuestionModel.Update(
            label = rep.label,
            helpText = rep.helpText,
            earliest = rep.earliest,
            latest = rep.latest
        )
        is FormTemplateTextQuestionRep.Update -> FormTemplateTextQuestionModel.Update(
            label = rep.label,
            helpText = rep.helpText,
            multiLine = rep.multiLine,
            placeholder = rep.placeholder,
            validator = rep.validator
        )
        is FormTemplateRadioSelectorQuestionRep.Update -> FormTemplateRadioSelectorQuestionModel.Update(
            label = rep.label,
            helpText = rep.helpText,
            options = rep.options
        )
        else -> unknownFormTemplateQuestion(rep::class)
    }

    private fun unknownFormTemplateQuestion(kClass: KClass<*>): Nothing {
        unknownType("form template question", kClass)
    }
}
