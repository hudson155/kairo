package io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import java.time.LocalDate
import java.util.UUID

object FormTemplateDateQuestionRep {

    data class Creation(
        override val label: String,
        override val helpText: String? = null,
        val earliest: LocalDate? = null,
        val latest: LocalDate? = null
    ) : FormTemplateQuestionRep.Creation

    data class Complete(
        override val id: UUID,
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
