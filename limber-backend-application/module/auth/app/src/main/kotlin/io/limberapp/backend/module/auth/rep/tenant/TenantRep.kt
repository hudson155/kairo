package io.limberapp.backend.module.auth.rep.tenant

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.validation.util.hostname
import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.mediumText
import java.time.LocalDateTime
import java.util.UUID

object TenantRep {

    data class Creation(
        val domain: String,
        val orgId: UUID,
        val auth0ClientId: String
    ) : CreationRep {
        override fun validate() {
            validate(TenantRep.Creation::domain) { hostname() }
            validate(TenantRep.Creation::auth0ClientId) { mediumText(allowEmpty = false) }
        }
    }

    data class Complete(
        val domain: String,
        override val created: LocalDateTime,
        val orgId: UUID,
        val auth0ClientId: String
    ) : CompleteRep

    data class Update(
        val domain: String? = null,
        val orgId: UUID? = null,
        val auth0ClientId: String? = null
    ) : UpdateRep {
        override fun validate() {
            validate(TenantRep.Update::domain) { ifPresent { hostname() } }
            validate(TenantRep.Update::auth0ClientId) { ifPresent { mediumText(allowEmpty = false) } }
        }
    }
}
