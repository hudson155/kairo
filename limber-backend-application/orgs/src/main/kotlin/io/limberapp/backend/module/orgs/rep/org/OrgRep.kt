package io.limberapp.backend.module.orgs.rep.org

import io.limberapp.framework.rep.CompleteRep
import io.limberapp.framework.rep.CreationRep
import io.limberapp.framework.rep.UpdateRep
import io.limberapp.framework.validation.validation.util.ifPresent
import io.limberapp.framework.validation.validation.util.shortText
import io.limberapp.framework.validation.validation.validate
import java.time.LocalDateTime
import java.util.UUID

object OrgRep {

    data class Creation(
        val name: String
    ) : CreationRep {
        override fun validate() {
            validate(Creation::name) { shortText(allowEmpty = false) }
        }
    }

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        override val version: Int,
        val name: String
    ) : CompleteRep

    data class Update(
        val name: String?
    ) : UpdateRep {
        override fun validate() {
            validate(Update::name) { ifPresent { shortText(allowEmpty = false) } }
        }
    }
}
