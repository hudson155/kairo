package io.limberapp.backend.module.auth.rep.tenant

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

object TenantRep {

    @Serializable
    data class Creation(
        val domain: String,
        @Serializable(with = UuidSerializer::class)
        val orgId: UUID,
        val auth0ClientId: String
    ) : CreationRep {
        override fun validate() = RepValidation {
            validate(Creation::domain) { Validator.hostname(value) }
            validate(Creation::auth0ClientId) { Validator.auth0ClientId(value) }
        }
    }

    @Serializable
    data class Complete(
        val domain: String,
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        @Serializable(with = UuidSerializer::class)
        val orgId: UUID,
        val auth0ClientId: String
    ) : CompleteRep

    @Serializable
    data class Update(
        val domain: String? = null,
        @Serializable(with = UuidSerializer::class)
        val orgId: UUID? = null,
        val auth0ClientId: String? = null
    ) : UpdateRep {
        override fun validate() = RepValidation {
            validate(Update::domain) { ifPresent { Validator.hostname(value) } }
            validate(Update::auth0ClientId) { ifPresent { Validator.auth0ClientId(value) } }
        }
    }
}
