package kairo.darb

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DarbEncoderTest {
  @Test
  fun encode(): Unit =
    runTest {
      DarbEncoder.encode(emptyList()).shouldBe("0.")
      DarbEncoder.encode(listOf(false)).shouldBe("1.0")
      DarbEncoder.encode(listOf(true)).shouldBe("1.8")
      DarbEncoder.encode(listOf(false, false, false, false)).shouldBe("4.0")
      DarbEncoder.encode(listOf(false, false, false, true)).shouldBe("4.1")
      DarbEncoder.encode(listOf(true, false, false, false)).shouldBe("4.8")
      DarbEncoder.encode(listOf(true, true, true, true)).shouldBe("4.F")
      DarbEncoder.encode(listOf(false, false, false, false, false)).shouldBe("5.00")
      DarbEncoder.encode(listOf(false, false, false, false, true)).shouldBe("5.08")
      DarbEncoder.encode(listOf(true, false, false, false, false)).shouldBe("5.80")
      DarbEncoder.encode(listOf(true, true, false, false, true)).shouldBe("5.C8")
      DarbEncoder.encode(listOf(true, true, true, true, true)).shouldBe("5.F8")
      DarbEncoder.encode(
        listOf(
          false, false, true, false,
          true, true, false, false,
          true, false, true, true,
          false, false, false, false,
          true, false, false, false,
          true, true, true,
        ),
      ).shouldBe("23.2CB08E")
    }

  @Test
  fun `decode, happy path`(): Unit =
    runTest {
      DarbEncoder.decode("0.").shouldBeEmpty()
      DarbEncoder.decode("1.0").shouldContainExactly(false)
      DarbEncoder.decode("1.8").shouldContainExactly(true)
      DarbEncoder.decode("4.0").shouldContainExactly(false, false, false, false)
      DarbEncoder.decode("4.1").shouldContainExactly(false, false, false, true)
      DarbEncoder.decode("4.8").shouldContainExactly(true, false, false, false)
      DarbEncoder.decode("4.F").shouldContainExactly(true, true, true, true)
      DarbEncoder.decode("4.f").shouldContainExactly(true, true, true, true)
      DarbEncoder.decode("5.00").shouldContainExactly(false, false, false, false, false)
      DarbEncoder.decode("5.08").shouldContainExactly(false, false, false, false, true)
      DarbEncoder.decode("5.80").shouldContainExactly(true, false, false, false, false)
      DarbEncoder.decode("5.C8").shouldContainExactly(true, true, false, false, true)
      DarbEncoder.decode("5.F8").shouldContainExactly(true, true, true, true, true)
      DarbEncoder.decode("5.f8").shouldContainExactly(true, true, true, true, true)
      DarbEncoder.decode("23.2CB08E").shouldBe(
        listOf(
          false, false, true, false,
          true, true, false, false,
          true, false, true, true,
          false, false, false, false,
          true, false, false, false,
          true, true, true,
        ),
      )
      DarbEncoder.decode("23.2cb08e").shouldBe(
        listOf(
          false, false, true, false,
          true, true, false, false,
          true, false, true, true,
          false, false, false, false,
          true, false, false, false,
          true, true, true,
        ),
      )
    }

  @Test
  fun `decode, error cases`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> { DarbEncoder.decode("") }
        .shouldHaveMessage("DARB must have 2 components.")
      shouldThrow<IllegalArgumentException> { DarbEncoder.decode("1.1.1") }
        .shouldHaveMessage("DARB must have 2 components.")
      shouldThrow<IllegalArgumentException> { DarbEncoder.decode("-1.") }
        .shouldHaveMessage("DARB size cannot be negative.")
      shouldThrow<IllegalArgumentException> { DarbEncoder.decode("0.1") }
        .shouldHaveMessage("DARB hex length doesn't match size component.")
      shouldThrow<IllegalArgumentException> { DarbEncoder.decode("1.") }
        .shouldHaveMessage("DARB hex length doesn't match size component.")
      shouldThrow<IllegalArgumentException> { DarbEncoder.decode("1.00") }
        .shouldHaveMessage("DARB hex length doesn't match size component.")
      shouldThrow<IllegalArgumentException> { DarbEncoder.decode("5.G8") }
        .shouldHaveMessage("Invalid DARB hex.")
    }
}
