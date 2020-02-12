package io.limberapp.backend.module.orgs.rep.org

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.mediumText
import java.time.LocalDateTime
import java.util.UUID

object OrgRep {

    data class Creation(
        val name: String
    ) : CreationRep {
        override fun validate() {
            validate(Creation::name) { mediumText(allowEmpty = false) }
        }
    }

    data class Complete(
        val id: UUID,
        override val created: LocalDateTime,
        val name: String,
        val features: Set<FeatureRep.Complete>,
        val members: Set<MembershipRep.Complete>
    ) : CompleteRep

    data class Update(
        val name: String? = null
    ) : UpdateRep {
        override fun validate() {
            validate(Update::name) { ifPresent { mediumText(allowEmpty = false) } }
        }
    }
}
