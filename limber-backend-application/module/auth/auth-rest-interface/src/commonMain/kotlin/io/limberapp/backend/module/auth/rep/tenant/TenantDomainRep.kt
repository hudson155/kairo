package io.limberapp.backend.module.auth.rep.tenant

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.validation.RepValidation
import com.piperframework.validator.Validator
import kotlinx.serialization.Serializable

object TenantDomainRep {

    @Serializable
    data class Creation(
        val domain: String
    ) : CreationRep {
        override fun validate() = RepValidation {
            validate(Creation::domain) { Validator.hostname(value) }
        }
    }

    @Serializable
    data class Complete(
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        val domain: String
    ) : CompleteRep
}
