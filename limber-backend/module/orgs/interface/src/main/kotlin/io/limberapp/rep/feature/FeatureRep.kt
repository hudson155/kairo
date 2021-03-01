package io.limberapp.rep.feature

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.Validator
import io.limberapp.validation.ifPresent
import java.util.UUID

object FeatureRep {
  enum class Type {
    FORMS,
    HOME;
  }

  data class Creation(
      val orgGuid: UUID,
      val name: String,
      val path: String,
      val type: Type,
      val rank: Int,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Creation::name) { Validator.featureName(this) }
      validate(Creation::path) { Validator.path(this) }
    }
  }

  data class Complete(
      val guid: UUID,
      val orgGuid: UUID,
      val name: String,
      val path: String,
      val type: Type,
      val rank: Int,
      val isDefaultFeature: Boolean,
  ) : CompleteRep

  data class Update(
      val name: String? = null,
      val path: String? = null,
      val rank: Int? = null,
      val isDefaultFeature: Boolean? = null,
  ) : UpdateRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Update::name) { ifPresent { Validator.featureName(this) } }
      validate(Update::path) { ifPresent { Validator.path(this) } }
    }
  }
}
