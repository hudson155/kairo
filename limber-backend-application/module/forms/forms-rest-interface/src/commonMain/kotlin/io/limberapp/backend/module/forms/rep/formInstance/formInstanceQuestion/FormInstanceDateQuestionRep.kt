package io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion

import com.piperframework.types.LocalDate
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep

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
