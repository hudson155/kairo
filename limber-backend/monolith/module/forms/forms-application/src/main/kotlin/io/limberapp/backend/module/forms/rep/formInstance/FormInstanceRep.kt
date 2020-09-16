package io.limberapp.backend.module.forms.rep.formInstance

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import kotlinx.serialization.Serializable

object FormInstanceRep {
  @Serializable
  data class Creation(
    @Serializable(with = UuidSerializer::class)
    val formTemplateGuid: UUID,
    @Serializable(with = UuidSerializer::class)
    val creatorAccountGuid: UUID,
  ) : CreationRep {
    override fun validate() = RepValidation {}
  }

  @Serializable
  data class Summary(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    val formTemplateGuid: UUID,
    val number: Long,
    @Serializable(with = LocalDateTimeSerializer::class)
    val submittedDate: LocalDateTime?,
    @Serializable(with = UuidSerializer::class)
    val creatorAccountGuid: UUID,
  ) : CompleteRep

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    val formTemplateGuid: UUID,
    val number: Long,
    @Serializable(with = LocalDateTimeSerializer::class)
    val submittedDate: LocalDateTime?,
    @Serializable(with = UuidSerializer::class)
    val creatorAccountGuid: UUID,
    val questions: Set<FormInstanceQuestionRep.Complete>,
  ) : CompleteRep

  @Serializable
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
