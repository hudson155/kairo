package io.limberapp.backend.module.orgs.rep.feature

import com.piperframework.rep.CompleteSubrep
import com.piperframework.rep.CreationSubrep
import com.piperframework.rep.UpdateRep
import com.piperframework.rep.UpdateSubrep
import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.path
import com.piperframework.validation.util.shortText
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.time.LocalDateTime
import java.util.UUID

object FeatureRep {

    data class Creation(
        val name: String,
        val path: String,
        val type: FeatureModel.Type
    ) : CreationSubrep {
        override fun validate() {
            validate(Creation::name) { shortText(allowEmpty = false) }
            validate(Creation::path) { path() }
        }
    }

    data class Complete(
        val id: UUID,
        val created: LocalDateTime,
        val name: String,
        val path: String,
        val type: FeatureModel.Type
    ) : CompleteSubrep

    data class Update(
        val name: String? = null,
        val path: String? = null
    ) : UpdateSubrep {
        override fun validate() {
            validate(Update::name) { ifPresent { shortText(allowEmpty = false) } }
            validate(Update::path) { ifPresent { path() } }
        }
    }
}
