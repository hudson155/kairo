package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validator.Validator

object FormInstanceTextQuestionRep {
  data class Creation(
    val text: String,
  ) : FormInstanceQuestionRep.Creation {
    override fun validate() = RepValidation {
      validate(Creation::text) { Validator.length10thousand(value, allowEmpty = false) }
    }
  }

  data class Complete(
    override val createdDate: LocalDateTime,
    override val questionGuid: UUID?,
    val text: String,
  ) : FormInstanceQuestionRep.Complete
}
