package io.limberapp.backend.module.forms.rep.formInstance

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.validation.RepValidation
import java.time.LocalDateTime
import java.util.UUID

internal object FormInstanceQuestionRep {

    interface Creation : CreationRep {

        val formTemplateQuestionId: UUID

        override fun validate() = RepValidation {}
    }

    interface Complete : CompleteRep {
        override val created: LocalDateTime
        val formTemplateQuestionId: UUID?
    }
}
