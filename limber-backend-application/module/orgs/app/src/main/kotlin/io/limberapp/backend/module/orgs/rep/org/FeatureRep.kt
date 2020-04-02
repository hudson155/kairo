package io.limberapp.backend.module.orgs.rep.org

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.time.LocalDateTime
import java.util.UUID

internal object FeatureRep {

    data class Creation(
        val name: String,
        val path: String,
        val type: FeatureModel.Type
    ) : CreationRep {
        override fun validate() = RepValidation {
            validate(Creation::name) { Validator.lengthCustom(value, minInclusive = 3, maxInclusive = 20) }
            validate(Creation::path) { Validator.path(value) }
        }
    }

    data class Complete(
        val id: UUID,
        override val created: LocalDateTime,
        val name: String,
        val path: String,
        val type: FeatureModel.Type,
        val isDefaultFeature: Boolean
    ) : CompleteRep

    data class Update(
        val name: String? = null,
        val path: String? = null,
        val isDefaultFeature: Boolean? = null
    ) : UpdateRep {
        override fun validate() = RepValidation {
            validate(Update::name) { ifPresent { Validator.lengthCustom(value, minInclusive = 3, maxInclusive = 20) } }
            validate(Update::path) { ifPresent { Validator.path(value) } }
        }
    }
}
