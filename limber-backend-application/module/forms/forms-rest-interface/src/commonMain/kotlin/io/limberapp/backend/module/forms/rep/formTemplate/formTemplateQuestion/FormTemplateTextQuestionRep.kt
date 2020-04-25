package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.RegexSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object FormTemplateTextQuestionRep {
    @Serializable
    @SerialName("TEXT")
    data class Creation(
        override val label: String,
        override val helpText: String? = null,
        val multiLine: Boolean,
        val placeholder: String? = null,
        @Serializable(with = RegexSerializer::class)
        val validator: Regex? = null
    ) : FormTemplateQuestionRep.Creation {
        override fun validate() = RepValidation {
            validate(super.validate())
            validate(Creation::placeholder) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
        }
    }

    @Serializable
    @SerialName("TEXT")
    data class Complete(
        @Serializable(with = UuidSerializer::class)
        override val guid: UUID,
        @Serializable(with = LocalDateTimeSerializer::class)
        override val createdDate: LocalDateTime,
        override val label: String,
        override val helpText: String?,
        val maxLength: Int,
        val multiLine: Boolean,
        val placeholder: String?,
        @Serializable(with = RegexSerializer::class)
        val validator: Regex?
    ) : FormTemplateQuestionRep.Complete

    @Serializable
    @SerialName("TEXT")
    data class Update(
        override val label: String? = null,
        override val helpText: String? = null,
        val multiLine: Boolean? = null,
        val placeholder: String? = null,
        @Serializable(with = RegexSerializer::class)
        val validator: Regex? = null
    ) : FormTemplateQuestionRep.Update {
        override fun validate() = RepValidation {
            validate(super.validate())
            validate(Update::placeholder) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
        }
    }
}
