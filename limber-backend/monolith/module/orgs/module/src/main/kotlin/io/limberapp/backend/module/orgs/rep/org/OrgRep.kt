package io.limberapp.backend.module.orgs.rep.org

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.ifPresent
import io.limberapp.common.validator.Validator
import java.time.LocalDateTime
import java.util.*

object OrgRep {
  data class Creation(
    val name: String,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::name) { Validator.orgName(value) }
    }
  }

  data class Complete(
    val guid: UUID,
    override val createdDate: LocalDateTime,
    val name: String,
    val ownerUserGuid: UUID?,
    val features: List<FeatureRep.Complete>,
  ) : CompleteRep

  data class Update(
    val name: String? = null,
    val ownerUserGuid: UUID? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::name) { ifPresent { Validator.orgName(value) } }
    }
  }
}
