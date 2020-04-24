package io.limberapp.backend.module.orgs.rep.org

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
import kotlinx.serialization.Serializable

object FeatureRep {
    enum class Type {
        FORMS,
        HOME;
    }

    @Serializable
    data class Creation(
        val name: String,
        val path: String,
        val type: Type
    ) : CreationRep {
        override fun validate() = RepValidation {
            validate(Creation::name) { Validator.featureName(value) }
            validate(Creation::path) { Validator.path(value) }
        }
    }

    @Serializable
    data class Complete(
        @Serializable(with = UuidSerializer::class)
        val id: UUID,
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        val name: String,
        val path: String,
        val type: Type,
        val isDefaultFeature: Boolean
    ) : CompleteRep

    @Serializable
    data class Update(
        val name: String? = null,
        val path: String? = null,
        val isDefaultFeature: Boolean? = null
    ) : UpdateRep {
        override fun validate() = RepValidation {
            validate(Update::name) { ifPresent { Validator.featureName(value) } }
            validate(Update::path) { ifPresent { Validator.path(value) } }
        }
    }
}
