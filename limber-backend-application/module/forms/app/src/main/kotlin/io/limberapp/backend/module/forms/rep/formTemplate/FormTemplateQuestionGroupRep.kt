package io.limberapp.backend.module.forms.rep.formTemplate

import com.piperframework.rep.CompleteSubrep
import java.util.UUID

object FormTemplateQuestionGroupRep {

    data class Complete(
        val id: UUID,
        val questions: List<FormTemplateQuestionRep.Complete>
    ) : CompleteSubrep
}
