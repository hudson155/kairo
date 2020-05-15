package io.limberapp.backend.module.forms.entity.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

internal data class FormTemplateQuestionEntity(
  val guid: UUID,
  val createdDate: LocalDateTime,
  val formTemplateGuid: UUID,
  val label: String,
  val helpText: String?,
  val type: FormTemplateQuestionModel.Type,
  val multiLine: Boolean?,
  val placeholder: String?,
  val validator: Regex?,
  val earliest: LocalDate?,
  val latest: LocalDate?,
  val options: List<String>?
) {
  internal data class Update(
    val label: String?,
    val helpText: String?,
    val multiLine: Boolean?,
    val placeholder: String?,
    val validator: Regex?,
    val earliest: LocalDate?,
    val latest: LocalDate?,
    val options: List<String>?
  ) {
    constructor(update: FormTemplateQuestionModel.Update) : this(
      label = update.label,
      helpText = update.helpText,
      multiLine = (update as? FormTemplateTextQuestionModel.Update)?.multiLine,
      placeholder = (update as? FormTemplateTextQuestionModel.Update)?.placeholder,
      validator = (update as? FormTemplateTextQuestionModel.Update)?.validator,
      earliest = (update as? FormTemplateDateQuestionModel.Update)?.earliest,
      latest = (update as? FormTemplateDateQuestionModel.Update)?.latest,
      options = (update as? FormTemplateRadioSelectorQuestionModel.Update)?.options
    )
  }

  constructor(model: FormTemplateQuestionModel) : this(
    guid = model.guid,
    createdDate = model.createdDate,
    formTemplateGuid = model.formTemplateGuid,
    label = model.label,
    helpText = model.helpText,
    type = model.type,
    multiLine = (model as? FormTemplateTextQuestionModel)?.multiLine,
    placeholder = (model as? FormTemplateTextQuestionModel)?.placeholder,
    validator = (model as? FormTemplateTextQuestionModel)?.validator,
    earliest = (model as? FormTemplateDateQuestionModel)?.earliest,
    latest = (model as? FormTemplateDateQuestionModel)?.latest,
    options = (model as? FormTemplateRadioSelectorQuestionModel)?.options
  )

  fun asModel() = when (type) {
    FormTemplateQuestionModel.Type.DATE -> FormTemplateDateQuestionModel(
      guid = guid,
      createdDate = createdDate,
      formTemplateGuid = formTemplateGuid,
      label = label,
      helpText = helpText,
      earliest = earliest,
      latest = latest
    )
    FormTemplateQuestionModel.Type.RADIO_SELECTOR -> FormTemplateRadioSelectorQuestionModel(
      guid = guid,
      createdDate = createdDate,
      formTemplateGuid = formTemplateGuid,
      label = label,
      helpText = helpText,
      options = checkNotNull(options)
    )
    FormTemplateQuestionModel.Type.TEXT -> FormTemplateTextQuestionModel(
      guid = guid,
      createdDate = createdDate,
      formTemplateGuid = formTemplateGuid,
      label = label,
      helpText = helpText,
      multiLine = checkNotNull(multiLine),
      placeholder = placeholder,
      validator = validator
    )
  }
}
