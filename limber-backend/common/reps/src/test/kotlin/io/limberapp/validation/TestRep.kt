package io.limberapp.validation

import io.limberapp.rep.ValidatedRep

private const val REQUIRED_STR_LENGTH: Int = 3

internal data class TestRep(
    val str: String,
    val int: Int?,
    val sub: TestSubRep,
    val subList: List<TestSubRep>,
    val subSet: Set<TestSubRep>,
) : ValidatedRep {
  override fun validate(): RepValidation = RepValidation {
    validate(TestRep::str) { length == REQUIRED_STR_LENGTH }
    validate(TestRep::int) { ifPresent { this < 0 } }
    validate(TestRep::sub)
    validate(TestRep::subList)
    validate(TestRep::subSet)
  }
}
