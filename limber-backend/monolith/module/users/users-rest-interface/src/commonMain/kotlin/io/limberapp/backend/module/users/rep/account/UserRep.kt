package io.limberapp.backend.module.users.rep.account

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import io.limberapp.common.serialization.serializer.UuidSerializer
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator
import kotlinx.serialization.Serializable

object UserRep {
  @Serializable
  data class Creation(
    @Serializable(with = UuidSerializer::class)
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

  @Serializable
  data class Summary(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    @Serializable(with = UuidSerializer::class)
    val orgGuid: UUID,
    val firstName: String?,
    val lastName: String?,
    val profilePhotoUrl: String?,
  ) : CompleteRep {
    val fullName get() = listOfNotNull(firstName, lastName).joinToString(" ")
  }

  @Serializable
  data class Complete(
    @Serializable(with = UuidSerializer::class)
    val guid: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    val roles: Set<JwtRole>,
    @Serializable(with = UuidSerializer::class)
    val orgGuid: UUID,
    val firstName: String?,
    val lastName: String?,
    val emailAddress: String,
    val profilePhotoUrl: String?,
  ) : CompleteRep {
    val fullName get() = listOfNotNull(firstName, lastName).joinToString(" ")
  }

  @Serializable
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
