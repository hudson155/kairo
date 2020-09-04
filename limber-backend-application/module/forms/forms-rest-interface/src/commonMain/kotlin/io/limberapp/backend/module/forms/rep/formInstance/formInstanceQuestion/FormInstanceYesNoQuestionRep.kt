package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object FormInstanceYesNoQuestionRep {
  @Serializable
  @SerialName("YES_NO")
  data class Creation(
    val yes: Boolean,
  ) : FormInstanceQuestionRep.Creation {
    override fun validate() = RepValidation {}
  }

  @Serializable
  @SerialName("YES_NO")
  data class Complete(
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    override val questionGuid: UUID?,
    val yes: Boolean,
  ) : FormInstanceQuestionRep.Complete
}
