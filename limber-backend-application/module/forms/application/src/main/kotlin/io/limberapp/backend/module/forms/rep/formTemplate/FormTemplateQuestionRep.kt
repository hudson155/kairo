package io.limberapp.backend.module.forms.rep.formTemplate

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
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

        override fun validate() = RepValidation {
            validate(Creation::label) { Validator.length1hundred(value, allowEmpty = false) }
            validate(Creation::helpText) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
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

        override fun validate() = RepValidation {
            validate(Update::label) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
            validate(Update::helpText) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
        }
    }
}
