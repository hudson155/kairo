package io.limberapp.backend.module.forms.rep.formTemplate

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateYesNoQuestionRep
import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator

object FormTemplateQuestionRep {
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(value = FormTemplateDateQuestionRep.Creation::class, name = "DATE"),
    JsonSubTypes.Type(value = FormTemplateRadioSelectorQuestionRep.Creation::class, name = "RADIO_SELECTOR"),
    JsonSubTypes.Type(value = FormTemplateTextQuestionRep.Creation::class, name = "TEXT"),
    JsonSubTypes.Type(value = FormTemplateYesNoQuestionRep.Creation::class, name = "YES_NO"),
  )
  interface Creation : CreationRep {
    val label: String
    val helpText: String?
    val required: Boolean

    override fun validate() = RepValidation {
      validate(Creation::label) { Validator.length1hundred(value, allowEmpty = false) }
      validate(Creation::helpText) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
    }
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(value = FormTemplateDateQuestionRep.Complete::class, name = "DATE"),
    JsonSubTypes.Type(value = FormTemplateRadioSelectorQuestionRep.Complete::class, name = "RADIO_SELECTOR"),
    JsonSubTypes.Type(value = FormTemplateTextQuestionRep.Complete::class, name = "TEXT"),
    JsonSubTypes.Type(value = FormTemplateYesNoQuestionRep.Complete::class, name = "YES_NO"),
  )
  interface Complete : CompleteRep {
    val guid: UUID
    val label: String
    val helpText: String?
    val required: Boolean
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(value = FormTemplateDateQuestionRep.Update::class, name = "DATE"),
    JsonSubTypes.Type(value = FormTemplateRadioSelectorQuestionRep.Update::class, name = "RADIO_SELECTOR"),
    JsonSubTypes.Type(value = FormTemplateTextQuestionRep.Update::class, name = "TEXT"),
    JsonSubTypes.Type(value = FormTemplateYesNoQuestionRep.Update::class, name = "YES_NO"),
  )
  interface Update : UpdateRep {
    val label: String?
    val helpText: String?
    val required: Boolean?

    override fun validate() = RepValidation {
      validate(Update::label) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
      validate(Update::helpText) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
    }
  }
}
