package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import java.time.LocalDateTime
import java.util.UUID

internal object FormInstanceTextQuestionRep {

    data class Creation(
        override val formTemplateQuestionId: UUID,
        val text: String
    ) : FormInstanceQuestionRep.Creation

    data class Complete(
        override val created: LocalDateTime,
        override val formTemplateQuestionId: UUID?,
        val text: String
    ) : FormInstanceQuestionRep.Complete
}
