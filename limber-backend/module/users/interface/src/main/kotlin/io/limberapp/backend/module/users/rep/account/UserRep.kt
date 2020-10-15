package io.limberapp.backend.module.users.rep.account

import io.limberapp.common.validator.Validator
import io.limberapp.permissions.AccountRole
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.ifPresent
import java.time.LocalDateTime
import java.util.*

object UserRep {
  data class Creation(
      val orgGuid: UUID,
      val firstName: String,
      val lastName: String,
      val emailAddress: String,
      val profilePhotoUrl: String? = null,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::firstName) { Validator.humanName(value) }
      validate(Creation::lastName) { Validator.humanName(value) }
      validate(Creation::emailAddress) { Validator.emailAddress(value) }
      validate(Creation::profilePhotoUrl) { ifPresent { Validator.url(value) } }
    }
  }

  data class Summary(
      val guid: UUID,
      override val createdDate: LocalDateTime,
      val orgGuid: UUID,
      val firstName: String?,
      val lastName: String?,
      val profilePhotoUrl: String?,
  ) : CompleteRep {
    val fullName get() = listOfNotNull(firstName, lastName).joinToString(" ")
  }

  data class Complete(
      val guid: UUID,
      override val createdDate: LocalDateTime,
      val roles: Set<AccountRole>,
      val orgGuid: UUID,
      val firstName: String?,
      val lastName: String?,
      val emailAddress: String,
      val profilePhotoUrl: String?,
  ) : CompleteRep {
    val fullName get() = listOfNotNull(firstName, lastName).joinToString(" ")
  }

  data class Update(
      val firstName: String? = null,
      val lastName: String? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::firstName) { ifPresent { Validator.humanName(value) } }
      validate(Update::lastName) { ifPresent { Validator.humanName(value) } }
    }
  }
}
