package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object FormInstanceTextQuestionRep {

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
