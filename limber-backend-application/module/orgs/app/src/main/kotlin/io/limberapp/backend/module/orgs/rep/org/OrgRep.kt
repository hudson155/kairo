package io.limberapp.backend.module.orgs.rep.org

import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.framework.rep.CompleteRep
import io.limberapp.framework.rep.CreationRep
import io.limberapp.framework.rep.UpdateRep
import io.limberapp.framework.validation.util.ifPresent
import io.limberapp.framework.validation.util.shortText
import java.time.LocalDateTime
import java.util.UUID

object OrgRep {

    data class Creation(
        val name: String
    ) : CreationRep() {
        override fun validate() {
            validate(Creation::name) { shortText(allowEmpty = false) }
        }
    }

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        val name: String,
        val members: List<MembershipRep.Complete>
    ) : CompleteRep()

    data class Update(
        val name: String? = null
    ) : UpdateRep() {
        override fun validate() {
            validate(Update::name) { ifPresent { shortText(allowEmpty = false) } }
        }
    }
}
