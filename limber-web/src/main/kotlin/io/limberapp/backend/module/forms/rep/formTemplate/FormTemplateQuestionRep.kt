package io.limberapp.backend.module.forms.rep.formTemplate

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator

object FormTemplateQuestionRep {
  interface Creation : CreationRep {
    val label: String
    val helpText: String?
    val required: Boolean

    override fun validate() = RepValidation {
      validate(Creation::label) { Validator.length1hundred(value, allowEmpty = false) }
      validate(Creation::helpText) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
    }
  }

  interface Complete : CompleteRep {
    val guid: String
    val label: String
    val helpText: String?
    val required: Boolean
  }

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
