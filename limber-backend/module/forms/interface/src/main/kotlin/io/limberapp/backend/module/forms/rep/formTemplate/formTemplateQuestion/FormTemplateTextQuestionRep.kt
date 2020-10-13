package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.common.validator.Validator
import io.limberapp.validation.RepValidation
import io.limberapp.validation.ifPresent
import java.time.LocalDateTime
import java.util.*

object FormTemplateTextQuestionRep {
  data class Creation(
      override val label: String,
      override val helpText: String? = null,
      override val required: Boolean,
      val multiLine: Boolean,
      val placeholder: String? = null,
      val validator: Regex? = null,
  ) : FormTemplateQuestionRep.Creation {
    override fun validate() = RepValidation {
      validate(super.validate())
      validate(Creation::placeholder) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
    }
  }

  data class Complete(
      override val guid: UUID,
      override val createdDate: LocalDateTime,
      override val label: String,
      override val helpText: String?,
      override val required: Boolean,
      val maxLength: Int,
      val multiLine: Boolean,
      val placeholder: String?,
      val validator: Regex?,
  ) : FormTemplateQuestionRep.Complete

  data class Update(
      override val label: String? = null,
      override val helpText: String? = null,
      override val required: Boolean? = null,
      val multiLine: Boolean? = null,
      val placeholder: String? = null,
      val validator: Regex? = null,
  ) : FormTemplateQuestionRep.Update {
    override fun validate() = RepValidation {
      validate(super.validate())
      validate(Update::placeholder) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
    }
  }
}
