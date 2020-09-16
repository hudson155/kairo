package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object FormTemplateRadioSelectorQuestionRep {
  @Serializable
  @SerialName("RADIO_SELECTOR")
  data class Creation(
    override val label: String,
    override val helpText: String? = null,
    override val required: Boolean,
    val options: List<String>,
  ) : FormTemplateQuestionRep.Creation {
    override fun validate() = RepValidation {
      validate(super.validate())
      validate(Creation::options) inner@{
        if (value.isEmpty()) return@inner false
        if (value.any { !Validator.length1hundred(it, allowEmpty = false) }) return@inner false
        return@inner value.distinct().size == value.size // All values must be distinct
      }
    }
  }

  @Serializable
  @SerialName("RADIO_SELECTOR")
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    override val guid: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    override val label: String,
    override val helpText: String?,
    override val required: Boolean,
    val options: List<String>,
  ) : FormTemplateQuestionRep.Complete

  @Serializable
  @SerialName("RADIO_SELECTOR")
  data class Update(
    override val label: String? = null,
    override val helpText: String? = null,
    override val required: Boolean? = null,
    val options: List<String>? = null,
  ) : FormTemplateQuestionRep.Update {
    override fun validate() = RepValidation {
      validate(super.validate())
      validate(Update::options) {
        ifPresent {
          if (value.isEmpty()) return@ifPresent false
          if (value.any { !Validator.length1hundred(it, allowEmpty = false) }) return@ifPresent false
          return@ifPresent value.distinct().size == value.size // All values must be distinct
        }
      }
    }
  }
}
