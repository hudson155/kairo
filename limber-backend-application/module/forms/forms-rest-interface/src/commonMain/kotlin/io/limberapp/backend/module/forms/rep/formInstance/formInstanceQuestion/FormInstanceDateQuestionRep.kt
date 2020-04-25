package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import com.piperframework.serialization.serializer.LocalDateSerializer
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDate
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object FormInstanceDateQuestionRep {
    @Serializable
    @SerialName("DATE")
    data class Creation(
        @Serializable(with = LocalDateSerializer::class)
        val date: LocalDate
    ) : FormInstanceQuestionRep.Creation

    @Serializable
    @SerialName("DATE")
    data class Complete(
        @Serializable(with = LocalDateTimeSerializer::class)
        override val createdDate: LocalDateTime,
        @Serializable(with = UuidSerializer::class)
        override val questionGuid: UUID?,
        @Serializable(with = LocalDateSerializer::class)
        val date: LocalDate
    ) : FormInstanceQuestionRep.Complete
}
