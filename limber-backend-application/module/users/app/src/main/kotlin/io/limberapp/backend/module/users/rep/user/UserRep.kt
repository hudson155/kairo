package io.limberapp.backend.module.users.rep.user

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.validation.util.emailAddress
import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.mediumText
import com.piperframework.validation.util.url
import io.limberapp.backend.authorization.principal.JwtRole
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
            validate(UserRep.Creation::firstName) { ifPresent { mediumText(allowEmpty = false) } }
            validate(UserRep.Creation::lastName) { ifPresent { mediumText(allowEmpty = false) } }
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
        val profilePhotoUrl: String?,
        val roles: Set<JwtRole>
    ) : CompleteRep()

    data class Update(
        val firstName: String? = null,
        val lastName: String? = null
    ) : UpdateRep() {
        override fun validate() {
            validate(UserRep.Update::firstName) { ifPresent { mediumText(allowEmpty = false) } }
            validate(UserRep.Update::lastName) { ifPresent { mediumText(allowEmpty = false) } }
        }
    }
}
