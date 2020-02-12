package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.mediumText
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import java.time.LocalDateTime
import java.util.UUID

internal object FormTemplateTextQuestionRep {

    data class Creation(
        override val label: String,
        override val helpText: String? = null,
        val multiLine: Boolean,
        val placeholder: String? = null,
        val validator: Regex? = null
    ) : FormTemplateQuestionRep.Creation {
        override fun validate() {
            super.validate()
            validate(Creation::placeholder) { ifPresent { mediumText(allowEmpty = false) } }
        }
    }

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        override val label: String,
        override val helpText: String?,
        val maxLength: Int,
        val multiLine: Boolean,
        val placeholder: String?,
        val validator: Regex?
    ) : FormTemplateQuestionRep.Complete

    data class Update(
        override val label: String? = null,
        override val helpText: String? = null,
        val multiLine: Boolean? = null,
        val placeholder: String? = null,
        val validator: Regex? = null
    ) : FormTemplateQuestionRep.Update {
        override fun validate() {
            super.validate()
            validate(Update::placeholder) { ifPresent { mediumText(allowEmpty = false) } }
        }
    }
}
