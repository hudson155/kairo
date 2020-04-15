package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal object FormTemplateRadioSelectorQuestionRep {

    @Serializable
    @SerialName("RADIO_SELECTOR")
    data class Creation(
        override val label: String,
        override val helpText: String? = null,
        val options: List<String>
    ) : FormTemplateQuestionRep.Creation {
        override fun validate() = RepValidation {
            validate(super.validate())
            validate(Creation::options) {
                value.isNotEmpty() && value.all {
                    Validator.length1hundred(
                        it,
                        allowEmpty = false
                    )
                } && value.count() == value.toSet().count()
            }
        }
    }

    @Serializable
    @SerialName("RADIO_SELECTOR")
    data class Complete(
        @Serializable(with = UuidSerializer::class)
        override val id: UUID,
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        override val label: String,
        override val helpText: String?,
        val options: List<String>
    ) : FormTemplateQuestionRep.Complete

    @Serializable
    @SerialName("RADIO_SELECTOR")
    data class Update(
        override val label: String? = null,
        override val helpText: String? = null,
        val options: List<String>? = null
    ) : FormTemplateQuestionRep.Update {
        override fun validate() = RepValidation {
            validate(super.validate())
            validate(Update::options) {
                ifPresent {
                    value.isNotEmpty() && value.all {
                        Validator.length1hundred(
                            it,
                            allowEmpty = false
                        )
                    } && value.count() == value.toSet().count()
                }
            }
        }
    }
}
