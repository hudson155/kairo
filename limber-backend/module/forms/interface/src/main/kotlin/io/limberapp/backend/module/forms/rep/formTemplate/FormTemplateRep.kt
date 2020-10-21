package io.limberapp.backend.module.forms.rep.formTemplate

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.Validator
import io.limberapp.common.validation.ifPresent
import java.time.ZonedDateTime
import java.util.*

object FormTemplateRep {
  data class Creation(
      val title: String,
      val description: String? = null,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::title) { Validator.length1hundred(value, allowEmpty = false) }
      validate(Creation::description) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
    }
  }

  data class Summary(
      val guid: UUID,
      override val createdDate: ZonedDateTime,
      val title: String,
      val description: String?,
  ) : CompleteRep

  data class Complete(
      val guid: UUID,
      override val createdDate: ZonedDateTime,
      val title: String,
      val description: String?,
      val questions: List<FormTemplateQuestionRep.Complete>,
  ) : CompleteRep

  data class Update(
      val title: String? = null,
      val description: String? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::title) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
      validate(Update::description) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
    }
  }
}
