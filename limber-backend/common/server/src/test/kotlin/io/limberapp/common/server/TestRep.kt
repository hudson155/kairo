package io.limberapp.common.server

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.validation.RepValidation

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
