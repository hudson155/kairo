package io.limberapp.backend.module.forms.rep.formInstance

import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import java.time.LocalDateTime
import java.util.*

object FormInstanceRep {
  data class Creation(
      val formTemplateGuid: UUID,
      val creatorAccountGuid: UUID,
  ) : CreationRep {
    override fun validate() = RepValidation {}
  }

  data class Summary(
      val guid: UUID,
      override val createdDate: LocalDateTime,
      val formTemplateGuid: UUID,
      val number: Long?,
      val submittedDate: LocalDateTime?,
      val creatorAccountGuid: UUID,
  ) : CompleteRep

  data class Complete(
      val guid: UUID,
      override val createdDate: LocalDateTime,
      val formTemplateGuid: UUID,
      val number: Long?,
      val submittedDate: LocalDateTime?,
      val creatorAccountGuid: UUID,
      val questions: Set<FormInstanceQuestionRep.Complete>,
  ) : CompleteRep

  data class Update(
      val submitted: Boolean? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::submitted) { ifPresent { value } }
    }
  }
}

fun FormInstanceRep.Complete.summary() = FormInstanceRep.Summary(
    guid = guid,
    createdDate = createdDate,
    formTemplateGuid = formTemplateGuid,
    number = number,
    submittedDate = submittedDate,
    creatorAccountGuid = creatorAccountGuid
)
