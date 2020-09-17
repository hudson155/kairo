package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation

object FormInstanceYesNoQuestionRep {
  data class Creation(
    val yes: Boolean,
  ) : FormInstanceQuestionRep.Creation {
    override fun validate() = RepValidation {}
  }

  data class Complete(
    override val createdDate: LocalDateTime,
    override val questionGuid: UUID?,
    val yes: Boolean,
  ) : FormInstanceQuestionRep.Complete
}
