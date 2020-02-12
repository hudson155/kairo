package io.limberapp.backend.module.forms.rep.formTemplate

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.longText
import com.piperframework.validation.util.mediumText
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import java.util.UUID

internal object FormTemplateQuestionRep {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormTemplateDateQuestionRep.Creation::class, name = "DATE"),
        JsonSubTypes.Type(value = FormTemplateTextQuestionRep.Creation::class, name = "TEXT")
    )
    interface Creation : CreationRep {

        val label: String
        val helpText: String?

        override fun validate() {
            validate(Creation::label) { mediumText(allowEmpty = false) }
            validate(Creation::helpText) { ifPresent { longText(allowEmpty = false) } }
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormTemplateDateQuestionRep.Complete::class, name = "DATE"),
        JsonSubTypes.Type(value = FormTemplateTextQuestionRep.Complete::class, name = "TEXT")
    )
    interface Complete : CompleteRep {
        val id: UUID
        val label: String
        val helpText: String?
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormTemplateDateQuestionRep.Update::class, name = "DATE"),
        JsonSubTypes.Type(value = FormTemplateTextQuestionRep.Update::class, name = "TEXT")
    )
    interface Update : UpdateRep {

        val label: String?
        val helpText: String?

        override fun validate() {
            validate(Update::label) { ifPresent { mediumText(allowEmpty = false) } }
            validate(Update::helpText) { ifPresent { longText(allowEmpty = false) } }
        }
    }
}
