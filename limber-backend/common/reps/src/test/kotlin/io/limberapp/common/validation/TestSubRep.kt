package io.limberapp.common.validation

import io.limberapp.common.rep.ValidatedRep

internal data class TestSubRep(
    val bool: Boolean,
) : ValidatedRep {
  override fun validate(): RepValidation = RepValidation {
    validate(TestSubRep::bool) { this }
  }
}
