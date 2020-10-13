package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.validation.RepValidation
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

object FormInstanceDateQuestionRep {
  data class Creation(
      val date: LocalDate,
  ) : FormInstanceQuestionRep.Creation {
    override fun validate() = RepValidation {}
  }

  data class Complete(
      override val createdDate: LocalDateTime,
      override val questionGuid: UUID?,
      val date: LocalDate,
  ) : FormInstanceQuestionRep.Complete
}
