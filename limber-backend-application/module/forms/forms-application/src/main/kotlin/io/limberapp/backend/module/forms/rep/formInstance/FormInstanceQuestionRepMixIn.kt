package io.limberapp.backend.module.forms.rep.formInstance

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep

internal object FormInstanceQuestionRepMixIn {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormInstanceDateQuestionRep.Creation::class, name = "DATE"),
        JsonSubTypes.Type(value = FormInstanceTextQuestionRep.Creation::class, name = "TEXT")
    )
    interface Creation : FormTemplateQuestionRep.Creation

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormInstanceDateQuestionRep.Complete::class, name = "DATE"),
        JsonSubTypes.Type(value = FormInstanceTextQuestionRep.Complete::class, name = "TEXT")
    )
    interface Complete : FormTemplateQuestionRep.Complete
}
