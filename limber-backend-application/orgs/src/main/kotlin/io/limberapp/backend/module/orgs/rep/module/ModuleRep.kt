package io.limberapp.backend.module.orgs.rep.module

import io.limberapp.framework.rep.CompleteRep
import io.limberapp.framework.rep.CreationRep
import io.limberapp.framework.rep.UpdateRep
import io.limberapp.framework.validation.validation.util.ifPresent
import io.limberapp.framework.validation.validation.util.shortText
import java.time.LocalDateTime
import java.util.UUID

object ModuleRep {

    enum class Type {
        FORM,
    }

    data class Creation(
        val name: String,
        val type: Type
    ) : CreationRep() {
        override fun validate() {
            validate(Creation::name) { shortText(allowEmpty = false) }
        }
    }

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        val name: String,
        val type: Type
    ) : CompleteRep()

    abstract class Update(
        val name: String?
    ) : UpdateRep() {
        override fun validate() {
            validate(Update::name) { ifPresent { shortText(allowEmpty = false) } }
        }
    }
}
