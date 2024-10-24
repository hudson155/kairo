package kairo.util

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoEqualsTest {
  @Suppress("EqualsOrHashCode", "EqualsWithHashCodeExist")
  private open class Subject(
    val something: String,
    val somethingElse: String,
  ) {
    override fun equals(other: Any?): Boolean =
      kairoEquals(
        other = other,
        properties = listOf(
          Subject::something,
          Subject::somethingElse,
        ),
      )
  }

  private class Subclass(
    something: String,
    somethingElse: String,
  ) : Subject(
    something = something,
    somethingElse = somethingElse,
  )

  @Test
  fun `happy path`(): Unit = runTest {
    Subject(something = "value 1", somethingElse = "value a")
      .shouldBe(Subject(something = "value 1", somethingElse = "value a"))
  }

  @Test
  fun `one property differs`(): Unit = runTest {
    Subject("value 1", "value a")
      .shouldNotBe(Subject(something = "value 2", somethingElse = "value a"))
    Subject("value 1", "value a")
      .shouldNotBe(Subject(something = "value 1", somethingElse = "value b"))
  }

  @Test
  fun `both properties differ`(): Unit = runTest {
    Subject("value 1", "value a")
      .shouldNotBe(Subject(something = "value 2", somethingElse = "value b"))
  }

  @Test
  fun `other is null`(): Unit = runTest {
    Subject(something = "value 1", somethingElse = "value a")
      .shouldNotBe(null)
  }

  @Test
  fun subclass(): Unit = runTest {
    Subject(something = "value 1", somethingElse = "value a")
      .shouldNotBe(Subclass(something = "value 1", somethingElse = "value a"))
  }
}
