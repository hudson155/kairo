package io.limberapp.common.validation

import io.limberapp.common.rep.ValidatedRep

private const val REQUIRED_STR_LENGTH: Int = 3

internal data class TestRep(
    val str: String,
    val int: Int?,
    val sub: TestSubRep,
    val subs: List<TestSubRep>,
) : ValidatedRep {
  override fun validate(): RepValidation = RepValidation {
    validate(TestRep::str) { length == REQUIRED_STR_LENGTH }
    validate(TestRep::int) { ifPresent { this < 0 } }
    validate(TestRep::sub)
    validate(TestRep::subs)
  }
}
