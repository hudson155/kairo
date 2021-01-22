package io.limberapp.backend.rep.typicodePost

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.validation.RepValidation

internal object TypicodePostRep {
  internal data class Creation(
      val userId: Int,
      val title: String,
      val body: String,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {}
  }

  internal data class Complete(
      val userId: Int,
      val id: Int,
      val title: String,
      val body: String,
  ) : CompleteRep
}
