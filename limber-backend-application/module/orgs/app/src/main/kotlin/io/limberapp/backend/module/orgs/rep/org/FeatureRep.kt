package io.limberapp.backend.module.orgs.rep.org

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.path
import com.piperframework.validation.util.shortText
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.time.LocalDateTime
import java.util.UUID

internal object FeatureRep {

    data class Creation(
        val name: String,
        val path: String,
        val type: FeatureModel.Type
    ) : CreationRep {
        override fun validate() {
            validate(Creation::name) { shortText(allowEmpty = false) }
            validate(Creation::path) { path() }
        }
    }

    data class Complete(
        val id: UUID,
        override val created: LocalDateTime,
        val name: String,
        val path: String,
        val type: FeatureModel.Type
    ) : CompleteRep

    data class Update(
        val name: String? = null,
        val path: String? = null
    ) : UpdateRep {
        override fun validate() {
            validate(Update::name) { ifPresent { shortText(allowEmpty = false) } }
            validate(Update::path) { ifPresent { path() } }
        }
    }
}
