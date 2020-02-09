package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

object FormInstanceDateQuestionRep {

    data class Creation(
        override val formTemplateQuestionId: UUID,
        val date: LocalDate
    ) : FormInstanceQuestionRep.Creation

    data class Complete(
        override val created: LocalDateTime,
        override val formTemplateQuestionId: UUID?,
        val date: LocalDate
    ) : FormInstanceQuestionRep.Complete
}
