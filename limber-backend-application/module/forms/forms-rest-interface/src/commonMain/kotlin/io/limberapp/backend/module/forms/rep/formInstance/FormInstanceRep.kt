package io.limberapp.backend.module.forms.rep.formInstance

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import kotlinx.serialization.Serializable

object FormInstanceRep {
  @Serializable
  data class Creation(
    @Serializable(with = UuidSerializer::class)
    val featureGuid: UUID,
    @Serializable(with = UuidSerializer::class)
    val formTemplateGuid: UUID
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
    val featureGuid: UUID,
    @Serializable(with = UuidSerializer::class)
    val formTemplateGuid: UUID,
    val number: Long
  ) : CompleteRep

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    val featureGuid: UUID,
    @Serializable(with = UuidSerializer::class)
    val formTemplateGuid: UUID,
    val number: Long,
    val questions: List<FormInstanceQuestionRep.Complete>
  ) : CompleteRep
}
