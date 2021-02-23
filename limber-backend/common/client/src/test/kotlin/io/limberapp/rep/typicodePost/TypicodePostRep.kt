package io.limberapp.rep.typicodePost

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.validation.RepValidation

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
