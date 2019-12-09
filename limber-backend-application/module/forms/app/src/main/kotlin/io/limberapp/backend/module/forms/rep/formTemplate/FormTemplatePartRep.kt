package io.limberapp.backend.module.forms.rep.formTemplate

import com.piperframework.rep.CompleteSubrep
import com.piperframework.rep.CreationSubrep
import com.piperframework.rep.UpdateSubrep
import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.mediumText
import java.util.UUID

object FormTemplatePartRep {

    data class Creation(
        val title: String? = null,
        val description: String? = null
    ) : CreationSubrep {
        override fun validate() {
            validate(Creation::title) { ifPresent { mediumText(allowEmpty = false) } }
            validate(Creation::description) { ifPresent { mediumText(allowEmpty = false) } }
        }
    }

    data class Complete(
        val id: UUID,
        val title: String?,
        val description: String?,
        val questionGroups: List<FormTemplateQuestionGroupRep.Complete>
    ) : CompleteSubrep

    data class Update(
        val title: String? = null,
        val description: String? = null
    ) : UpdateSubrep {
        override fun validate() {
            validate(Update::title) { ifPresent { mediumText(allowEmpty = false) } }
            validate(Update::description) { ifPresent { mediumText(allowEmpty = false) } }
        }
    }
}
