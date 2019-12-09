package io.limberapp.backend.module.forms.rep.formTemplate

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.longText
import com.piperframework.validation.util.mediumText
import java.time.LocalDateTime
import java.util.UUID

object FormTemplateRep {

    data class Creation(
        val orgId: UUID,
        val title: String,
        val description: String? = null
    ) : CreationRep {
        override fun validate() {
            validate(Creation::title) { mediumText(allowEmpty = false) }
            validate(Creation::description) { ifPresent { longText(allowEmpty = false) } }
        }
    }

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        val orgId: UUID,
        val title: String,
        val description: String?,
        val parts: List<FormTemplatePartRep.Complete>
    ) : CompleteRep

    data class Update(
        val title: String? = null,
        val description: String? = null
    ) : UpdateRep {
        override fun validate() {
            validate(Update::title) { ifPresent { mediumText(allowEmpty = false) } }
            validate(Update::description) { ifPresent { longText(allowEmpty = false) } }
        }
    }
}
