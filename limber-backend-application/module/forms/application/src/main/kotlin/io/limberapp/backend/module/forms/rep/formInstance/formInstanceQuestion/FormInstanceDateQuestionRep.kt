package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import com.piperframework.serialization.LocalDateSerializer
import com.piperframework.serialization.UuidSerializer
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

internal object FormInstanceDateQuestionRep {

    @Serializable
    @SerialName("DATE")
    data class Creation(
        @Serializable(with = UuidSerializer::class)
        override val formTemplateQuestionId: UUID,
        @Serializable(with = LocalDateSerializer::class)
        val date: LocalDate
    ) : FormInstanceQuestionRep.Creation

    @Serializable
    @SerialName("DATE")
    data class Complete(
        @Serializable(with = LocalDateSerializer::class)
        override val created: LocalDateTime,
        @Serializable(with = UuidSerializer::class)
        override val formTemplateQuestionId: UUID?,
        @Serializable(with = LocalDateSerializer::class)
        val date: LocalDate
    ) : FormInstanceQuestionRep.Complete
}
