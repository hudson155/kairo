package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import com.piperframework.serialization.LocalDateSerializer
import com.piperframework.serialization.LocalDateTimeSerializer
import com.piperframework.serialization.UuidSerializer
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

internal object FormTemplateDateQuestionRep {

    @Serializable
    @SerialName("DATE")
    data class Creation(
        override val label: String,
        override val helpText: String? = null,
        @Serializable(with = LocalDateSerializer::class)
        val earliest: LocalDate? = null,
        @Serializable(with = LocalDateSerializer::class)
        val latest: LocalDate? = null
    ) : FormTemplateQuestionRep.Creation

    @Serializable
    @SerialName("DATE")
    data class Complete(
        @Serializable(with = UuidSerializer::class)
        override val id: UUID,
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        override val label: String,
        override val helpText: String?,
        @Serializable(with = LocalDateSerializer::class)
        val earliest: LocalDate?,
        @Serializable(with = LocalDateSerializer::class)
        val latest: LocalDate?
    ) : FormTemplateQuestionRep.Complete

    @Serializable
    @SerialName("DATE")
    data class Update(
        override val label: String? = null,
        override val helpText: String? = null,
        @Serializable(with = LocalDateSerializer::class)
        val earliest: LocalDate? = null,
        @Serializable(with = LocalDateSerializer::class)
        val latest: LocalDate? = null
    ) : FormTemplateQuestionRep.Update
}
