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
        val featureId: UUID,
        @Serializable(with = UuidSerializer::class)
        val formTemplateId: UUID
    ) : CreationRep {
        override fun validate() = RepValidation {}
    }

    @Serializable
    data class Complete(
        @Serializable(with = UuidSerializer::class)
        val id: UUID,
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        @Serializable(with = UuidSerializer::class)
        val featureId: UUID,
        @Serializable(with = UuidSerializer::class)
        val formTemplateId: UUID,
        val questions: List<FormInstanceQuestionRep.Complete>
    ) : CompleteRep
}
