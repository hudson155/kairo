package io.limberapp.validation

import io.limberapp.rep.ValidatedRep

internal data class TestSubRep(
    val bool: Boolean,
) : ValidatedRep {
  override fun validate(): RepValidation = RepValidation {
    validate(TestSubRep::bool) { this }
  }
}
