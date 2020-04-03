package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

internal object FormInstanceTextQuestionRep {

    @Serializable
    @SerialName("TEXT")
    data class Creation(
        @Serializable(with = UuidSerializer::class)
        override val formTemplateQuestionId: UUID,
        val text: String
    ) : FormInstanceQuestionRep.Creation

    @Serializable
    @SerialName("TEXT")
    data class Complete(
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        @Serializable(with = UuidSerializer::class)
        override val formTemplateQuestionId: UUID?,
        val text: String
    ) : FormInstanceQuestionRep.Complete
}
