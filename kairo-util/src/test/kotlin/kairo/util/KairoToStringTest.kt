package kairo.util

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoToStringTest {
  @Suppress("EqualsOrHashCode")
  private open class Subject(
    val something: String,
    val somethingElse: String,
  ) {
    override fun toString(): String =
      kairoToString(
        name = "Subject",
        properties = listOf(
          "something" to something,
          "somethingElse" to somethingElse,
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
    Subject(something = "value 1", somethingElse = "value a").toString()
      .shouldBe("Subject(something=value 1, somethingElse=value a)")
  }

  @Test
  fun subclass(): Unit = runTest {
    Subclass(something = "value 1", somethingElse = "value a").toString()
      .shouldBe("Subject(something=value 1, somethingElse=value a)")
  }
}
