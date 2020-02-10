package io.limberapp.backend.module.forms.rep.formInstance

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import java.time.LocalDateTime
import java.util.UUID

object FormInstanceQuestionRep {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormInstanceDateQuestionRep.Creation::class, name = "DATE"),
        JsonSubTypes.Type(value = FormInstanceTextQuestionRep.Creation::class, name = "TEXT")
    )
    interface Creation : CreationRep {
        val formTemplateQuestionId: UUID
        override fun validate() = Unit
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = FormInstanceDateQuestionRep.Complete::class, name = "DATE"),
        JsonSubTypes.Type(value = FormInstanceTextQuestionRep.Complete::class, name = "TEXT")
    )
    interface Complete : CompleteRep {
        override val created: LocalDateTime
        val formTemplateQuestionId: UUID?
    }
}
