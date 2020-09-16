package io.limberapp.backend.module.forms.rep.formTemplate

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator
import kotlinx.serialization.Serializable

object FormTemplateRep {
  @Serializable
  data class Creation(
    val title: String,
    val description: String? = null,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::title) { Validator.length1hundred(value, allowEmpty = false) }
      validate(Creation::description) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
    }
  }

  @Serializable
  data class Summary(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    val title: String,
    val description: String?,
  ) : CompleteRep

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    val title: String,
    val description: String?,
    val questions: List<FormTemplateQuestionRep.Complete>,
  ) : CompleteRep

  @Serializable
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
