package io.limberapp.rep.org

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.rep.feature.FeatureRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.Validator
import io.limberapp.validation.ifPresent
import java.util.UUID

object OrgRep {
  data class Creation(
      val name: String,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Creation::name) { Validator.orgName(this) }
    }
  }

  data class Complete(
      val guid: UUID,
      val name: String,
      val ownerUserGuid: UUID?,
      val features: List<FeatureRep.Complete>,
  ) : CompleteRep

  data class Update(
      val name: String? = null,
      val ownerUserGuid: UUID? = null,
  ) : UpdateRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Update::name) { ifPresent { Validator.orgName(this) } }
    }
  }
}
