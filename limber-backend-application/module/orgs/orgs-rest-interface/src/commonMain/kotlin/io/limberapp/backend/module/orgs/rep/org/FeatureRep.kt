package io.limberapp.backend.module.orgs.rep.org

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator

object FeatureRep {

    enum class Type {
        FORMS,
        HOME;
    }

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

    data class Complete(
        val id: UUID,
        override val created: LocalDateTime,
        val name: String,
        val path: String,
        val type: Type,
        val isDefaultFeature: Boolean
    ) : CompleteRep

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
