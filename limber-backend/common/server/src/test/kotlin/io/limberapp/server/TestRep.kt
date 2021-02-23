package io.limberapp.server

import io.limberapp.rep.CompleteRep
import io.limberapp.rep.CreationRep
import io.limberapp.validation.RepValidation

internal object TestRep {
  internal data class Creation(
      val foo: String,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {
      validate(Creation::foo) { isNotEmpty() }
    }
  }

  internal data class Complete(
      val foo: String,
  ) : CompleteRep
}
