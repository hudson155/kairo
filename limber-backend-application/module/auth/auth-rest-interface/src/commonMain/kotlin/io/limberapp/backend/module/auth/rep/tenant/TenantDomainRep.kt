package io.limberapp.backend.module.auth.rep.tenant

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.types.LocalDateTime
import com.piperframework.validation.RepValidation
import com.piperframework.validator.Validator

object TenantDomainRep {

    data class Creation(
        val domain: String
    ) : CreationRep {
        override fun validate() = RepValidation {
            validate(Creation::domain) { Validator.hostname(value) }
        }
    }

    data class Complete(
        override val created: LocalDateTime,
        val domain: String
    ) : CompleteRep
}
