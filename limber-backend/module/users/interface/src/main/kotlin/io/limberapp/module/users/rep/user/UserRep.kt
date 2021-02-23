package io.limberapp.module.users.rep.user

import io.limberapp.permissions.limber.LimberPermissions
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.Validator
import io.limberapp.validation.ifPresent
import java.util.UUID

object UserRep {
  data class Creation(
      val orgGuid: UUID,
      val emailAddress: String,
      val firstName: String,
      val lastName: String,
      val profilePhotoUrl: String? = null,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Creation::emailAddress) { Validator.emailAddress(this) }
      validate(Creation::firstName) { Validator.humanName(this) }
      validate(Creation::lastName) { Validator.humanName(this) }
      validate(Creation::profilePhotoUrl) { ifPresent { Validator.url(this) } }
    }
  }

  data class Summary(
      val guid: UUID,
      val orgGuid: UUID,
      val firstName: String,
      val lastName: String,
      val fullName: String,
      val profilePhotoUrl: String?,
  ) : CompleteRep

  data class Complete(
      val guid: UUID,
      val permissions: LimberPermissions,
      val orgGuid: UUID,
      val emailAddress: String,
      val firstName: String,
      val lastName: String,
      val fullName: String,
      val profilePhotoUrl: String?,
  ) : CompleteRep

  data class Update(
      val permissions: LimberPermissions? = null,
      val firstName: String? = null,
      val lastName: String? = null,
  ) : UpdateRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Update::firstName) { ifPresent { Validator.humanName(this) } }
      validate(Update::lastName) { ifPresent { Validator.humanName(this) } }
    }
  }
}
