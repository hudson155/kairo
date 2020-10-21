package io.limberapp.backend.module.forms.rep.formInstance

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import java.time.ZonedDateTime
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
      override val createdDate: ZonedDateTime,
      val formTemplateGuid: UUID,
      val number: Long?,
      val submittedDate: ZonedDateTime?,
      val creatorAccountGuid: UUID,
  ) : CompleteRep

  data class Complete(
      val guid: UUID,
      override val createdDate: ZonedDateTime,
      val formTemplateGuid: UUID,
      val number: Long?,
      val submittedDate: ZonedDateTime?,
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
