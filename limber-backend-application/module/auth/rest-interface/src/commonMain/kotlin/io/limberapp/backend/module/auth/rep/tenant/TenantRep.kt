package io.limberapp.backend.module.auth.rep.tenant

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator

object TenantRep {

    data class Creation(
        val domain: String,
        val orgId: UUID,
        val auth0ClientId: String
    ) : CreationRep {
        override fun validate() = RepValidation {
            validate(Creation::domain) { Validator.hostname(value) }
            validate(Creation::auth0ClientId) { Validator.auth0ClientId(value) }
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
        override fun validate() = RepValidation {
            validate(Update::domain) { ifPresent { Validator.hostname(value) } }
            validate(Update::auth0ClientId) { ifPresent { Validator.auth0ClientId(value) } }
        }
    }
}
