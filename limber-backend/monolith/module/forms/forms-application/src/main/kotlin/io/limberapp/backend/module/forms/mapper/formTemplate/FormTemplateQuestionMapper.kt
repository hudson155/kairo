package io.limberapp.backend.module.forms.mapper.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateYesNoQuestionModel
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateYesNoQuestionRep
import io.limberapp.common.util.unknownType
import io.limberapp.common.util.uuid.UuidGenerator
import java.time.Clock
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass

private const val TEXT_QUESTION_MAX_LENGTH_MULTILINE = 10_000
private const val TEXT_QUESTION_MAX_LENGTH_ONE_LINE = 200

internal class FormTemplateQuestionMapper @Inject constructor(
  private val clock: Clock,
  private val uuidGenerator: UuidGenerator,
) {
  fun model(formTemplateGuid: UUID, rep: FormTemplateQuestionRep.Creation) = when (rep) {
    is FormTemplateDateQuestionRep.Creation -> FormTemplateDateQuestionModel(
      guid = uuidGenerator.generate(),
      createdDate = LocalDateTime.now(clock),
      formTemplateGuid = formTemplateGuid,
      label = rep.label,
      helpText = rep.helpText,
      required = rep.required,
      earliest = rep.earliest,
      latest = rep.latest,
    )
    is FormTemplateRadioSelectorQuestionRep.Creation -> FormTemplateRadioSelectorQuestionModel(
      guid = uuidGenerator.generate(),
      createdDate = LocalDateTime.now(clock),
      formTemplateGuid = formTemplateGuid,
      label = rep.label,
      helpText = rep.helpText,
      required = rep.required,
      options = rep.options,
    )
    is FormTemplateTextQuestionRep.Creation -> FormTemplateTextQuestionModel(
      guid = uuidGenerator.generate(),
      createdDate = LocalDateTime.now(clock),
      formTemplateGuid = formTemplateGuid,
      label = rep.label,
      helpText = rep.helpText,
      required = rep.required,
      multiLine = rep.multiLine,
      placeholder = rep.placeholder,
      validator = rep.validator,
    )
    is FormTemplateYesNoQuestionRep.Creation -> FormTemplateYesNoQuestionModel(
      guid = uuidGenerator.generate(),
      createdDate = LocalDateTime.now(clock),
      formTemplateGuid = formTemplateGuid,
      label = rep.label,
      helpText = rep.helpText,
      required = rep.required,
    )
    else -> unknownFormTemplateQuestion(rep::class)
  }

  fun completeRep(model: FormTemplateQuestionModel) = when (model) {
    is FormTemplateDateQuestionModel -> FormTemplateDateQuestionRep.Complete(
      guid = model.guid,
      createdDate = model.createdDate,
      label = model.label,
      helpText = model.helpText,
      required = model.required,
      earliest = model.earliest,
      latest = model.latest,
    )
    is FormTemplateRadioSelectorQuestionModel -> FormTemplateRadioSelectorQuestionRep.Complete(
      guid = model.guid,
      createdDate = model.createdDate,
      label = model.label,
      helpText = model.helpText,
      required = model.required,
      options = model.options,
    )
    is FormTemplateTextQuestionModel -> FormTemplateTextQuestionRep.Complete(
      guid = model.guid,
      createdDate = model.createdDate,
      label = model.label,
      helpText = model.helpText,
      required = model.required,
      maxLength = if (model.multiLine) TEXT_QUESTION_MAX_LENGTH_MULTILINE else TEXT_QUESTION_MAX_LENGTH_ONE_LINE,
      multiLine = model.multiLine,
      placeholder = model.placeholder,
      validator = model.validator,
    )
    is FormTemplateYesNoQuestionModel -> FormTemplateYesNoQuestionRep.Complete(
      guid = model.guid,
      createdDate = model.createdDate,
      label = model.label,
      helpText = model.helpText,
      required = model.required,
    )
    else -> unknownFormTemplateQuestion(model::class)
  }

  fun update(rep: FormTemplateQuestionRep.Update) = when (rep) {
    is FormTemplateDateQuestionRep.Update -> FormTemplateDateQuestionModel.Update(
      label = rep.label,
      helpText = rep.helpText,
      required = rep.required,
      earliest = rep.earliest,
      latest = rep.latest
    )
    is FormTemplateRadioSelectorQuestionRep.Update -> FormTemplateRadioSelectorQuestionModel.Update(
      label = rep.label,
      helpText = rep.helpText,
      required = rep.required,
      options = rep.options
    )
    is FormTemplateTextQuestionRep.Update -> FormTemplateTextQuestionModel.Update(
      label = rep.label,
      helpText = rep.helpText,
      required = rep.required,
      multiLine = rep.multiLine,
      placeholder = rep.placeholder,
      validator = rep.validator
    )
    else -> unknownFormTemplateQuestion(rep::class)
  }

  private fun unknownFormTemplateQuestion(kClass: KClass<*>): Nothing {
    unknownType("form template question", kClass)
  }
}
