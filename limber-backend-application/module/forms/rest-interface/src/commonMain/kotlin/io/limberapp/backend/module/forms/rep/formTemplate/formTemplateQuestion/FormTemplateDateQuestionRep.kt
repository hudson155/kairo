package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import com.piperframework.types.LocalDate
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep

object FormTemplateDateQuestionRep {

    data class Creation(
        override val label: String,
        override val helpText: String? = null,
        val earliest: LocalDate? = null,
        val latest: LocalDate? = null
    ) : FormTemplateQuestionRep.Creation

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        override val label: String,
        override val helpText: String?,
        val earliest: LocalDate?,
        val latest: LocalDate?
    ) : FormTemplateQuestionRep.Complete

    data class Update(
        override val label: String? = null,
        override val helpText: String? = null,
        val earliest: LocalDate? = null,
        val latest: LocalDate? = null
    ) : FormTemplateQuestionRep.Update
}
