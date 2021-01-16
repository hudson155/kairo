package io.limberapp.common.validation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class RepValidationTest {
  @Test
  fun `happy path`(): Unit = assertValid(
      rep = TestRep("abc", -3, TestSubRep(true), listOf(TestSubRep(true), TestSubRep(true))),
  )

  @Test
  fun `happy path - missing ifPresent`(): Unit = assertValid(
      rep = TestRep("abc", null, TestSubRep(true), listOf(TestSubRep(true), TestSubRep(true))),
  )

  @Test
  fun `multiple errors`(): Unit = assertInvalid(
      rep = TestRep("ab", 3, TestSubRep(true), listOf(TestSubRep(true), TestSubRep(true))),
      invalidPropertyNames = listOf("str", "int"),
  )

  @Test
  fun `error in sub-rep (single)`(): Unit = assertInvalid(
      rep = TestRep("abc", -3, TestSubRep(false), listOf(TestSubRep(true), TestSubRep(true))),
      invalidPropertyNames = listOf("sub.bool"),
  )

  @Test
  fun `error in sub-rep (list)`(): Unit = assertInvalid(
      rep = TestRep("abc", -3, TestSubRep(true), listOf(TestSubRep(true), TestSubRep(false))),
      invalidPropertyNames = listOf("subs[1].bool"),
  )

  private fun assertValid(rep: TestRep) {
    val validation = rep.validate()
    assertTrue(validation.isValid)
    assertFails { validation.invalidPropertyNames }
  }

  private fun assertInvalid(rep: TestRep, invalidPropertyNames: List<String>) {
    val validation = rep.validate()
    assertFalse(validation.isValid)
    assertEquals(invalidPropertyNames, validation.invalidPropertyNames)
  }
}
