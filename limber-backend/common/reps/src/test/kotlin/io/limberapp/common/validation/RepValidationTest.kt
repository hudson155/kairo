package io.limberapp.common.validation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class RepValidationTest {
  @Test
  fun `happy path`(): Unit = assertValid(
      rep = TestRep(
          str = "abc",
          int = -3,
          sub = TestSubRep(bool = true),
          subs = listOf(TestSubRep(bool = true), TestSubRep(bool = true)),
      ),
  )

  @Test
  fun `happy path - missing ifPresent`(): Unit = assertValid(
      rep = TestRep(
          str = "abc",
          int = null,
          sub = TestSubRep(bool = true),
          subs = listOf(TestSubRep(bool = true), TestSubRep(bool = true)),
      ),
  )

  @Test
  fun `multiple errors`(): Unit = assertInvalid(
      rep = TestRep(
          str = "ab",
          int = 3,
          sub = TestSubRep(bool = true),
          subs = listOf(TestSubRep(bool = true), TestSubRep(bool = true)),
      ),
      invalidPropertyNames = listOf("str", "int"),
  )

  @Test
  fun `error in sub-rep (single)`(): Unit = assertInvalid(
      rep = TestRep(
          str = "abc",
          int = -3,
          sub = TestSubRep(bool = false),
          subs = listOf(TestSubRep(bool = true), TestSubRep(bool = true)),
      ),
      invalidPropertyNames = listOf("sub.bool"),
  )

  @Test
  fun `error in sub-rep (list)`(): Unit = assertInvalid(
      rep = TestRep(
          str = "abc",
          int = -3,
          sub = TestSubRep(bool = true),
          subs = listOf(TestSubRep(bool = true), TestSubRep(bool = false)),
      ),
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
