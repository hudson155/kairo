package io.limberapp.backend.module.orgs.rep.feature

import com.piperframework.rep.CompleteSubrep
import com.piperframework.rep.CreationSubrep
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
    ) : CreationSubrep() {
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
    ) : CompleteSubrep()
}
