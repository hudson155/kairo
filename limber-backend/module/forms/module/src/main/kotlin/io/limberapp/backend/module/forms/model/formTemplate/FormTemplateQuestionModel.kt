package io.limberapp.backend.module.forms.model.formTemplate

import java.time.ZonedDateTime
import java.util.*

interface FormTemplateQuestionModel {
  val guid: UUID
  val createdDate: ZonedDateTime
  val formTemplateGuid: UUID
  val label: String
  val helpText: String?
  val required: Boolean
  val type: Type

  enum class Type {
    DATE,
    RADIO_SELECTOR,
    TEXT,
    YES_NO,
  }

  interface Update {
    val label: String?
    val helpText: String?
    val required: Boolean?
  }
}
