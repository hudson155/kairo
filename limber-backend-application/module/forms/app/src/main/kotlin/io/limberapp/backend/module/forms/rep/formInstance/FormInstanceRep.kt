package io.limberapp.backend.module.forms.rep.formInstance

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import java.time.LocalDateTime
import java.util.UUID

object FormInstanceRep {

    data class Creation(
        val orgId: UUID,
        val formTemplateId: UUID,
        val questions: List<FormInstanceQuestionRep.Creation>
    ) : CreationRep {
        override fun validate() {
            validate(Creation::questions) { subject.forEach { it.validate() } }
        }
    }

    data class Complete(
        val id: UUID,
        override val created: LocalDateTime,
        val orgId: UUID,
        val formTemplateId: UUID,
        val questions: List<FormInstanceQuestionRep.Complete>
    ) : CompleteRep
}
