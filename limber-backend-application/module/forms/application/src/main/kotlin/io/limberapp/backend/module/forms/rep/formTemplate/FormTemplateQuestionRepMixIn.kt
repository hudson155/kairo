package io.limberapp.backend.module.forms.rep.formTemplate

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep

internal object FormTemplateQuestionRepMixIn {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormTemplateDateQuestionRep.Creation::class, name = "DATE"),
        JsonSubTypes.Type(value = FormTemplateTextQuestionRep.Creation::class, name = "TEXT")
    )
    interface Creation : FormTemplateQuestionRep.Complete

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormTemplateDateQuestionRep.Complete::class, name = "DATE"),
        JsonSubTypes.Type(value = FormTemplateTextQuestionRep.Complete::class, name = "TEXT")
    )
    interface Complete : FormTemplateQuestionRep.Creation

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormTemplateDateQuestionRep.Update::class, name = "DATE"),
        JsonSubTypes.Type(value = FormTemplateTextQuestionRep.Update::class, name = "TEXT")
    )
    interface Update : FormTemplateQuestionRep.Update
}
