package io.limberapp.backend.module.users.rep.account

import com.fasterxml.jackson.annotation.JsonIgnore
import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.Validator
import io.limberapp.common.validation.ifPresent
import io.limberapp.permissions.AccountRole
import java.time.ZonedDateTime
import java.util.*

object UserRep {
  data class Creation(
      val orgGuid: UUID,
      val firstName: String?,
      val lastName: String?,
      val emailAddress: String,
      val profilePhotoUrl: String? = null,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::firstName) { ifPresent { Validator.humanName(value) } }
      validate(Creation::lastName) { ifPresent { Validator.humanName(value) } }
      validate(Creation::emailAddress) { Validator.emailAddress(value) }
      validate(Creation::profilePhotoUrl) { ifPresent { Validator.url(value) } }
    }
  }

  data class Summary(
      val guid: UUID,
      override val createdDate: ZonedDateTime,
      val orgGuid: UUID,
      val firstName: String?,
      val lastName: String?,
      val fullName: String?,
      val profilePhotoUrl: String?,
  ) : CompleteRep

  data class Complete(
      val guid: UUID,
      override val createdDate: ZonedDateTime,
      val roles: Set<AccountRole>,
      val orgGuid: UUID,
      val firstName: String?,
      val lastName: String?,
      val fullName: String?,
      val emailAddress: String,
      val profilePhotoUrl: String?,
  ) : CompleteRep

  data class Update(
      val identityProvider: Boolean? = null,
      val superuser: Boolean? = null,
      val firstName: String? = null,
      val lastName: String? = null,
  ) : UpdateRep {
    val specifiesAccountRoleAdditions: Boolean
      @JsonIgnore get() = setOf(identityProvider, superuser).any { it == true }

    override fun validate() = RepValidation {
      validate(Update::firstName) { ifPresent { Validator.humanName(value) } }
      validate(Update::lastName) { ifPresent { Validator.humanName(value) } }
    }
  }
}
