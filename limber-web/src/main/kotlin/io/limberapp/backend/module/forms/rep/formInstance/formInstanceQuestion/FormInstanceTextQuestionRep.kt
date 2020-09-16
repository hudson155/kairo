package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validator.Validator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object FormInstanceTextQuestionRep {
  @Serializable
  @SerialName("TEXT")
  data class Creation(
    val text: String,
  ) : FormInstanceQuestionRep.Creation {
    override fun validate() = RepValidation {
      validate(Creation::text) { Validator.length10thousand(value, allowEmpty = false) }
    }
  }

  @Serializable
  @SerialName("TEXT")
  data class Complete(
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    override val questionGuid: String?,
    val text: String,
  ) : FormInstanceQuestionRep.Complete
}
