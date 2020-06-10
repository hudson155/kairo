package io.limberapp.backend.module.forms.rep.formTemplate

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
import kotlinx.serialization.Serializable

object FormTemplateRep {
  @Serializable
  data class Creation(
    val title: String,
    val description: String? = null
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
    @Serializable(with = UuidSerializer::class)
    val featureGuid: UUID,
    val title: String,
    val description: String?
  ) : CompleteRep

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    val featureGuid: UUID,
    val title: String,
    val description: String?,
    val questions: List<FormTemplateQuestionRep.Complete>
  ) : CompleteRep

  @Serializable
  data class Update(
    val title: String? = null,
    val description: String? = null
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::title) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
      validate(Update::description) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
    }
  }
}
