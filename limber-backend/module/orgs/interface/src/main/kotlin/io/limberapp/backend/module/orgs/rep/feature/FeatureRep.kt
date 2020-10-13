package io.limberapp.backend.module.orgs.rep.feature

import io.limberapp.common.validator.Validator
import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.rep.UpdateRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.ifPresent
import java.time.LocalDateTime
import java.util.*

object FeatureRep {
  enum class Type {
    FORMS,
    HOME;
  }

  data class Creation(
      val rank: Int,
      val name: String,
      val path: String,
      val type: Type,
  ) : CreationRep {
    override fun validate() = RepValidation {
      validate(Creation::name) { Validator.featureName(value) }
      validate(Creation::path) { Validator.path(value) }
    }
  }

  data class Complete(
      val guid: UUID,
      override val createdDate: LocalDateTime,
      val rank: Int,
      val name: String,
      val path: String,
      val type: Type,
      val isDefaultFeature: Boolean,
  ) : CompleteRep

  data class Update(
      val rank: Int? = null,
      val name: String? = null,
      val path: String? = null,
      val isDefaultFeature: Boolean? = null,
  ) : UpdateRep {
    override fun validate() = RepValidation {
      validate(Update::name) { ifPresent { Validator.featureName(value) } }
      validate(Update::path) { ifPresent { Validator.path(value) } }
    }
  }
}

val List<FeatureRep.Complete>.default get() = singleOrNull { it.isDefaultFeature }
