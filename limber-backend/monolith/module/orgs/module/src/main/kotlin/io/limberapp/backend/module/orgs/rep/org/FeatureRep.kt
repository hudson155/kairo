package io.limberapp.backend.module.orgs.rep.org

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.rep.UpdateRep
import io.limberapp.common.validation.RepValidation
import io.limberapp.common.validation.ifPresent
import io.limberapp.common.validator.Validator
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
