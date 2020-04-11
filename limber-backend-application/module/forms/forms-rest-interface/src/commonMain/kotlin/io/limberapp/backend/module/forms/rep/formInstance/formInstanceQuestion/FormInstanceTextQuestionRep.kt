package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep

object FormInstanceTextQuestionRep {

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
