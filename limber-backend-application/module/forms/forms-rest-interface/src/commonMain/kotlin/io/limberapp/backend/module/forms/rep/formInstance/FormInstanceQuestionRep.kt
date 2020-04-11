package io.limberapp.backend.module.forms.rep.formInstance

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation

object FormInstanceQuestionRep {

    interface Creation : CreationRep {

        val formTemplateQuestionId: UUID

        override fun validate() = RepValidation {}
    }

    interface Complete : CompleteRep {
        override val created: LocalDateTime
        val formTemplateQuestionId: UUID?
    }
}
