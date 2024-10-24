package kairo.util

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoHashCodeTest {
  @Suppress("EqualsOrHashCode")
  private open class Subject(
    val something: String,
    val somethingElse: String,
  ) {
    override fun hashCode(): Int =
      kairoHashCode(
        something,
        somethingElse,
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
    Subject(something = "value 1", somethingElse = "value a").hashCode()
      .shouldBe(Subject(something = "value 1", somethingElse = "value a").hashCode())
  }

  @Test
  fun implementation(): Unit = runTest {
    Subject(something = "value 1", somethingElse = "value a").hashCode()
      .shouldBe(31 * "value 1".hashCode() + "value a".hashCode())
  }

  @Test
  fun `one property differs`(): Unit = runTest {
    Subject("value 1", "value a").hashCode()
      .shouldNotBe(Subject(something = "value 2", somethingElse = "value a").hashCode())
    Subject("value 1", "value a").hashCode()
      .shouldNotBe(Subject(something = "value 1", somethingElse = "value b").hashCode())
  }

  @Test
  fun `both properties differ`(): Unit = runTest {
    Subject("value 1", "value a").hashCode()
      .shouldNotBe(Subject(something = "value 2", somethingElse = "value b").hashCode())
  }

  @Test
  fun `other is null`(): Unit = runTest {
    Subject(something = "value 1", somethingElse = "value a").hashCode()
      .shouldNotBe(null.hashCode())
  }

  @Test
  fun subclass(): Unit = runTest {
    Subject(something = "value 1", somethingElse = "value a").hashCode()
      .shouldBe(Subclass(something = "value 1", somethingElse = "value a").hashCode())
  }
}
