package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.RegexSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object FormTemplateTextQuestionRep {
  @Serializable
  @SerialName("TEXT")
  data class Creation(
    override val label: String,
    override val helpText: String? = null,
    override val required: Boolean,
    val multiLine: Boolean,
    val placeholder: String? = null,
    @Serializable(with = RegexSerializer::class)
    val validator: Regex? = null,
  ) : FormTemplateQuestionRep.Creation {
    override fun validate() = RepValidation {
      validate(super.validate())
      validate(Creation::placeholder) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
    }
  }

  @Serializable
  @SerialName("TEXT")
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    override val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    override val label: String,
    override val helpText: String?,
    override val required: Boolean,
    val maxLength: Int,
    val multiLine: Boolean,
    val placeholder: String?,
    @Serializable(with = RegexSerializer::class)
    val validator: Regex?,
  ) : FormTemplateQuestionRep.Complete

  @Serializable
  @SerialName("TEXT")
  data class Update(
    override val label: String? = null,
    override val helpText: String? = null,
    override val required: Boolean? = null,
    val multiLine: Boolean? = null,
    val placeholder: String? = null,
    @Serializable(with = RegexSerializer::class)
    val validator: Regex? = null,
  ) : FormTemplateQuestionRep.Update {
    override fun validate() = RepValidation {
      validate(super.validate())
      validate(Update::placeholder) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
    }
  }
}
