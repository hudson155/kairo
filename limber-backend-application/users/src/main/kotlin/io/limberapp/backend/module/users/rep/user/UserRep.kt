package io.limberapp.backend.module.users.rep.user

import io.limberapp.framework.rep.CompleteRep
import io.limberapp.framework.rep.CreationRep
import io.limberapp.framework.rep.UpdateRep
import io.limberapp.framework.validation.util.emailAddress
import io.limberapp.framework.validation.util.ifPresent
import io.limberapp.framework.validation.util.shortText
import io.limberapp.framework.validation.util.url
import java.time.LocalDateTime
import java.util.UUID

object UserRep {

    data class Creation(
        val firstName: String?,
        val lastName: String?,
        val emailAddress: String,
        val profilePhotoUrl: String?
    ) : CreationRep() {
        override fun validate() {
            validate(UserRep.Creation::firstName) { ifPresent { shortText(allowEmpty = false) } }
            validate(UserRep.Creation::lastName) { ifPresent { shortText(allowEmpty = false) } }
            validate(UserRep.Creation::emailAddress) { emailAddress() }
            validate(UserRep.Creation::profilePhotoUrl) { ifPresent { url() } }
        }
    }

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        val firstName: String?,
        val lastName: String?,
        val emailAddress: String,
        val profilePhotoUrl: String?
    ) : CompleteRep()

    data class Update(
        val firstName: String? = null,
        val lastName: String? = null
    ) : UpdateRep() {
        override fun validate() {
            validate(UserRep.Update::firstName) { ifPresent { shortText(allowEmpty = false) } }
            validate(UserRep.Update::lastName) { ifPresent { shortText(allowEmpty = false) } }
        }
    }
}
