package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import com.piperframework.serialization.serializer.LocalDateSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import com.piperframework.validator.Validator
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object FormInstanceRadioSelectorQuestionRep {

    @Serializable
    @SerialName("RADIO_SELECTOR")
    data class Creation(
        val selection: Set<String>
    ) : FormInstanceQuestionRep.Creation {
        override fun validate() = RepValidation {
            validate(super.validate())
            validate(Creation::selection) { value.all { Validator.length1hundred(it, allowEmpty = false) } }
        }
    }

    @Serializable
    @SerialName("RADIO_SELECTOR")
    data class Complete(
        @Serializable(with = LocalDateSerializer::class)
        override val created: LocalDateTime,
        @Serializable(with = UuidSerializer::class)
        override val formTemplateQuestionId: UUID?,
        val selection: Set<String>
    ) : FormInstanceQuestionRep.Complete
}
