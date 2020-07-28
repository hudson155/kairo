package io.limberapp.backend.module.forms.model.formTemplate

import java.time.LocalDateTime
import java.util.*

interface FormTemplateQuestionModel {
  val guid: UUID
  val createdDate: LocalDateTime
  val formTemplateGuid: UUID
  val label: String
  val helpText: String?
  val required: Boolean
  val type: Type

  enum class Type {
    DATE,
    RADIO_SELECTOR,
    TEXT,
  }

  interface Update {
    val label: String?
    val helpText: String?
    val required: Boolean?
  }
}
