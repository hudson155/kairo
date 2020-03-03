package io.limberapp.backend.module.forms.rep.formInstance

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import java.time.LocalDateTime
import java.util.UUID

internal object FormInstanceRep {

    data class Creation(
        val featureId: UUID,
        val formTemplateId: UUID
    ) : CreationRep {
        override fun validate() = Unit
    }

    data class Complete(
        val id: UUID,
        override val created: LocalDateTime,
        val featureId: UUID,
        val formTemplateId: UUID,
        val questions: List<FormInstanceQuestionRep.Complete>
    ) : CompleteRep
}
