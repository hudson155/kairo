package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validator.Validator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object FormInstanceRadioSelectorQuestionRep {
  @Serializable
  @SerialName("RADIO_SELECTOR")
  data class Creation(
    val selection: String,
  ) : FormInstanceQuestionRep.Creation {
    override fun validate() = RepValidation {
      validate(Creation::selection) { Validator.length1hundred(value, allowEmpty = false) }
    }
  }

  @Serializable
  @SerialName("RADIO_SELECTOR")
  data class Complete(
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    override val questionGuid: String?,
    val selection: String,
  ) : FormInstanceQuestionRep.Complete
}
