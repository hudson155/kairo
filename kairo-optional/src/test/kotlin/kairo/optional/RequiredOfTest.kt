package kairo.optional

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RequiredOfTest {
  @Test
  fun test(): Unit =
    runTest {
      Required.of(42)
        .shouldBe(Required.Value(42))
    }
}
